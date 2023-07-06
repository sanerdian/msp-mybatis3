package com.jnetdata.msp.media.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.core.util.ExcelBean;
import com.jnetdata.msp.core.util.ExcelUtil;
import com.jnetdata.msp.media.mapper.SurveyMapper;
import com.jnetdata.msp.media.service.SurveyService;
import com.jnetdata.msp.media.util.ReflactUtil;
import com.jnetdata.msp.media.vo.Lz_WorkerVo;
import com.jnetdata.msp.media.vo.survey.*;
import com.jnetdata.msp.member.user.mapper.UserMapper;
import com.jnetdata.msp.member.user.model.User;
import com.jnetdata.msp.tlujy.answer.mapper.AnswerMapper;
import com.jnetdata.msp.tlujy.answer.model.Answer;
import com.jnetdata.msp.tlujy.answer_user.mapper.AnswerUserMapper;
import com.jnetdata.msp.tlujy.answer_user.model.AnswerUser;
import com.jnetdata.msp.tlujy.investigate.mapper.InvestigateMapper;
import com.jnetdata.msp.tlujy.investigate.model.Investigate;
import com.jnetdata.msp.tlujy.investigate_user.mapper.InvestigateUserMapper;
import com.jnetdata.msp.tlujy.investigate_user.model.InvestigateUser;
import com.jnetdata.msp.tlujy.topic.mapper.TopicMapper;
import com.jnetdata.msp.tlujy.topic.model.Topic;
import com.jnetdata.msp.vo.BaseVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.thenicesys.data.api.Pager;
import org.thenicesys.data.api.Pair;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@Service
public class SurveyServiceImpl implements SurveyService {
    @Autowired
    private InvestigateMapper investigateMapper;
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private AnswerMapper answerMapper;
    @Autowired
    private AnswerUserMapper answerUserMapper;
    @Autowired
    private InvestigateUserMapper investigateUserMapper;
    @Autowired
    private SurveyMapper surveyMapper;
    @Autowired
    private UserMapper userMapper;
    private Lz_WorkerVo getCurrentWorker(){
        Object obj= SecurityUtils.getSubject().getSession().getAttribute("userinfo");

        if(obj!=null){
            Lz_WorkerVo user= (Lz_WorkerVo)obj ;
            return user;
        }else {
            return null;
        }
    }
    private Long getUserId() {//todo 尝试获取用户id，失败则用默认值。测试时临时使用。正式功能时，非登陆用户应该抛异常，让其登陆
        try{
            Lz_WorkerVo worker = getCurrentWorker();
            return Long.parseLong(worker.getExternalid());
        }catch (Exception e){
            return 0L;
        }

    }
    @Override
    public Long save(InvestigateVo vo) {
        Long id = vo.getId();
        if(id==null){//无id，新增模式
            investigateMapper.insert(vo);//新增
        }else {//修改模式
            id=vo.getId();
            HashMap map = new HashMap();
            map.put("investigateid",id);
            answerMapper.deleteByMap(map);//删除备选答案
            topicMapper.deleteByMap(map);//删除题目
            investigateMapper.updateById(vo);//修改
        }
        id=vo.getId();
        List<TopicVo> topics = vo.getTopics();
        if(topics!=null){
            for(int indexTopic=0;indexTopic<topics.size();indexTopic++){
                TopicVo topic=topics.get(indexTopic);
                topic.setSeqencing(indexTopic);
                topic.setInvestigateid(id);
                topicMapper.insert(topic);//保存题目
                Long topicId = topic.getId();
                List<Answer> answers = topic.getAnswers();
                if(answers!=null){
                    for(int indexAnswer=0;indexAnswer<answers.size();indexAnswer++){
                        Answer answer=answers.get(indexAnswer);
                        answer.setInvestigateid(id);
                        answer.setTopicid(topicId);
                        answer.setSeqencing(indexAnswer);
                        answerMapper.insert(answer);//保存选择题的备选答案
                    }
                }
            }
        }
        return vo.getId();
    }

    @Override
    public InvestigateVo getById(Long id) {
        Investigate i = investigateMapper.selectById(id);
        InvestigateVo vo= (InvestigateVo) ReflactUtil.obj2obj(i,InvestigateVo.class,true);

        List<TopicVo> topicVos = new ArrayList<>();
        List<Topic> topicList = topicMapper.selectList(new PropertyWrapper<>(Topic.class).eq("investigateid",id).orderBy(Arrays.asList(new String[]{"seqencing"}),true).getWrapper());
        List<Answer> answerList = answerMapper.selectList(new PropertyWrapper<>(Answer.class).eq("investigateid",id).orderBy(Arrays.asList(new String[]{"topicid","seqencing"}),true).getWrapper());
        if(topicList!=null){
            HashMap<Long,List<Answer>> mapAnswers = new HashMap<>();//key为题目id，value为选择题的备选答案列表
            if(answerList!=null){
                for(Answer answer:answerList){
                    Long topicId=answer.getTopicid();
                    List<Answer> answers;
                    if(mapAnswers.containsKey(topicId)){
                        answers = mapAnswers.get(topicId);
                    }else {
                        answers = new ArrayList<>();
                        mapAnswers.put(topicId,answers);
                    }
                    answers.add(answer);
                }
            }
            Map<String, List> mapUserAnswer = getAnswers(id);
            for(Topic topic:topicList){//准备主题
                TopicVo topicVo = (TopicVo) ReflactUtil.obj2obj(topic, TopicVo.class, true);
                List<Answer> answers=mapAnswers.get(topic.getId());

                if(answers!=null){
                    topicVo.setAnswers(answers);
                    List userAnswer=mapUserAnswer.get(String.valueOf(topic.getId()));
                    topicVo.setUserAnswer(ObjectUtils.isEmpty(userAnswer)?new ArrayList():userAnswer);
                }
                topicVos.add(topicVo);
            }
        }
        vo.setTopics(topicVos);

        PropertyWrapper wrapper=new PropertyWrapper<>(InvestigateUser.class);
        wrapper.eq("investigateid",id);
        wrapper.eq("userid",getUserId());
        wrapper.andNew();
        wrapper.isNull("recoverstatus");
        wrapper.or();
        wrapper.eq("recoverstatus",0);
        List<InvestigateUser> investigateUsers=investigateUserMapper.selectList(wrapper.getWrapper());
        if(vo.getTimelimitflag()!=null&&vo.getTimelimitflag()>0){//如果限制答题时间，则尝试从问卷-用户表中获取剩余答题时间
            if(!ObjectUtils.isEmpty(investigateUsers)){
                Long secondRest=investigateUsers.get(0).getSecondrest();
                if(secondRest!=null&&secondRest>=0){
                    vo.setTimelimitsecond(secondRest);
                }
            }
        }

        PropertyWrapper wrapper2=new PropertyWrapper<>(InvestigateUser.class);
        wrapper2.eq("investigateid",id);
        wrapper2.eq("userid",getUserId());
        wrapper2.eq("recoverstatus",1);

        List<InvestigateUser> investigateUsers2=investigateUserMapper.selectList(wrapper2.getWrapper());

        if(investigateUsers2!=null&&investigateUsers2.size()>0){
            vo.setIsCommit(1);
        }else{
            vo.setIsCommit(0);
        }

        return vo;
    }

    @Override
    public void deletesOnDisk(List<Long> ids) {
        PropertyWrapper wrapper = new PropertyWrapper(Answer.class);
        wrapper.in("investigateid",ids);
        answerMapper.delete(wrapper.getWrapper());

        wrapper=new PropertyWrapper(Topic.class);
        wrapper.in("investigateid",ids);
        topicMapper.delete(wrapper.getWrapper());

        wrapper=new PropertyWrapper(Investigate.class);
        wrapper.in("id",ids);
        investigateMapper.delete(wrapper.getWrapper());
    }

    @Override
    public void deletesByRemark(List<Long> ids) {
        PropertyWrapper wrapper=new PropertyWrapper(Investigate.class);
        wrapper.in("id",ids);
        List<Investigate> investigates = investigateMapper.selectList(wrapper.getWrapper());
        if(investigates!=null){
            for(Investigate investigate:investigates){
                investigate.setDocstatus(1);
                investigateMapper.updateById(investigate);
            }
        }
    }

    @Override
    public void updates(List<Investigate> investigates) {
        for(Investigate investigate:investigates){
            investigateMapper.updateById(investigate);
        }
    }

    @Override
    public Map<String, List> getAnswers(Long id) {
        Long userid= getUserId();
        PropertyWrapper wrapper=new PropertyWrapper(AnswerUser.class);
        wrapper.eq("investigateid",id);
        wrapper.eq("createBy",userid);
        List<AnswerUser> answerUsers = answerUserMapper.selectList(wrapper.getWrapper());
        Map<String,List> result= new HashMap();
        if(answerUsers!=null){
            for(AnswerUser answerUser:answerUsers){
                String topicId=String.valueOf(answerUser.getTopicid());
                Object answer= ObjectUtils.isEmpty(answerUser.getAnswerchoose())?answerUser.getAnswertext():answerUser.getAnswerchoose();
                if(answer!=null){
                    answer=answer.toString();
                }
                List answers;
                if(result.containsKey(topicId)){
                    answers=result.get(topicId);
                }else {
                    answers=new ArrayList();
                    result.put(topicId,answers);
                }
                answers.add(answer);
            }
        }
        return result;
    }

    @Override
    public void saveAnswers(Long id, Map<Long, List> answerMap,Long secondRest) {
        if(ObjectUtils.isEmpty(answerMap)){
            throw new RuntimeException("至少需要保存一道题目的答案！");
        }
        //获取当前用户之前对本问卷的答题状况
        Long userid= getUserId();
        PropertyWrapper wrapper=new PropertyWrapper(InvestigateUser.class);
        wrapper.eq("investigateid",id);
        wrapper.eq("userid",userid);
        wrapper.andNew();
        wrapper.isNull("recoverstatus");
        wrapper.or();
        wrapper.eq("recoverstatus",0);
        List<InvestigateUser> investigateUsers = investigateUserMapper.selectList(wrapper.getWrapper());
        InvestigateUser investigateUser;
        //如果获取InvestigateUser失败，则重新创建
        if(ObjectUtils.isEmpty(investigateUsers)){
            investigateUser = new InvestigateUser();
            investigateUser.setUserid(String.valueOf(userid));
            //todo 可能需要填充用户名称、所在部门的信息
            investigateUser.setInvestigateid(id);
            investigateUser.setRecoverstatus(0);
            investigateUserMapper.insert(investigateUser);
        }else {
            investigateUser=investigateUsers.get(0);
        }
        Set<Long> answeredTopics=new HashSet<>(answerMap.keySet());//存已答题目的id
        List<Long> invalidAnswer=new ArrayList<>();//存已失效的id
        //获取之前保存的答案
        wrapper=new PropertyWrapper(AnswerUser.class);
        wrapper.eq("investigateid",id);
        wrapper.eq("createBy",userid);
        wrapper.eq("recoverstatus",0);
        List<AnswerUser> answerUsers=answerUserMapper.selectList(wrapper.getWrapper());
        for(AnswerUser answerUser:answerUsers){
            if(answerMap.containsKey(answerUser.getTopicid())){
                invalidAnswer.add(answerUser.getId());
            }else {
                answeredTopics.add(answerUser.getTopicid());
            }
        }
        if(secondRest!=null){
            investigateUser.setSecondrest(secondRest);//保存剩余答题时间
        }
        investigateUser.setAnswerednum(answeredTopics.size());//保存已回答题目数量
        investigateUserMapper.updateById(investigateUser);

        if(invalidAnswer.size()>0){
            answerUserMapper.deleteBatchIds(invalidAnswer);//题目上次已答过，先删除上次的答案
        }
        //获取每道题的题目类型，用于保存
        Map<Long,Long> mapTopic= new HashMap<>();//key为题目id，value为题目类型，0单选题，1多选题，2多行文本题
        List<Topic> topics  = topicMapper.selectList(new PropertyWrapper(Topic.class).eq("investigateid",id).getWrapper());
        for(Topic topic:topics){
            mapTopic.put(topic.getId(),topic.getState());
        }

        //遍历为每一个题目保存所有答案
        for(Long topicId:answerMap.keySet()){
            List answers=answerMap.get(topicId);
            if(answers!=null&&answers.size()>0){
                Long stat=mapTopic.get(topicId);
                if(stat==null){
                    stat=1L;//默认为多选
                }
                for(Object object:answers){
                    AnswerUser answerUser=new AnswerUser();
                    answerUser.setCreateBy(userid);
                    answerUser.setInvestigateid(id);
                    answerUser.setTopicid(topicId);
                    answerUser.setRecoverstatus(0);
                    if(object!=null){
                        if(0L==stat||1L==stat){
                            Long value=Long.parseLong(String.valueOf(object));
                            answerUser.setAnswerchoose(value);
                        }else if(2L==stat){
                            String value=String.valueOf(object);
                            answerUser.setAnswertext(value);
                        }else {
                            throw new RuntimeException("未定义的题目类型："+stat);
                        }
                    }
                    answerUserMapper.insert(answerUser);//每个答案保存一行记录
                }
            }
        }

    }

    @Override
    public void commitAnswers(Long investigateid) {
        Long userid=getUserId();
        PropertyWrapper wrapper = new PropertyWrapper(AnswerUser.class);
        wrapper.eq("investigateid",investigateid);
        wrapper.eq("createBy",userid);
        wrapper.andNew();
        wrapper.isNull("recoverstatus");
        wrapper.or();
        wrapper.eq("recoverstatus",0);
        List<AnswerUser> answerUsers=answerUserMapper.selectList(wrapper.getWrapper());
        for(AnswerUser answerUser:answerUsers){
            answerUser.setRecoverstatus(1);
            answerUserMapper.updateById(answerUser);
        }
        wrapper = new PropertyWrapper(InvestigateUser.class);
        wrapper.eq("investigateid",investigateid);
        wrapper.eq("userid",userid);
        wrapper.andNew();
        wrapper.isNull("recoverstatus");
        wrapper.or();
        wrapper.eq("recoverstatus",0);
        List<InvestigateUser> investigateUsers=investigateUserMapper.selectList(wrapper.getWrapper());
        for(InvestigateUser investigateUser:investigateUsers){//理论上应该只有一条记录，毕竟该表存的是用户-问卷
            investigateUser.setRecoverstatus(1);
            investigateUserMapper.updateById(investigateUser);//更新用户-问卷状态为已回收
        }

    }

    private InvestigateUser createInvestigateUser(Long investigateid, Long userid) {
        InvestigateUser investigateUser=new InvestigateUser();
        investigateUser.setInvestigateid(investigateid);
        investigateUser.setUserid(String.valueOf(userid));

        return investigateUser;
    }

    @Override
    public List<InvestigateCurrentVo> getSurvey4CurrentUser(Investigate template) {
        List<InvestigateCurrentVo> vos = new ArrayList<>();//存结果

        Long userid = getUserId();
        List<InvestigateUser> investigateUsers = investigateUserMapper
                .selectList(new PropertyWrapper<>(InvestigateUser.class).eq("userid", userid).getWrapper());
        //获取当前用户对问卷的答题情况
        Map<Long,InvestigateUser> mapInvestigateUser= new HashMap<>();
        for(InvestigateUser investigateUser:investigateUsers){
            Long investigateid=investigateUser.getInvestigateid();
            if(mapInvestigateUser.containsKey(investigateid)){//一份问卷答了多次，取最后的一次
                if(mapInvestigateUser.get(investigateid).getCreateTime().before(investigateUser.getCreateTime())){
                    mapInvestigateUser.put(investigateid,investigateUser);
                }
            }else {
                mapInvestigateUser.put(investigateid,investigateUser);
            }
        }

        //获取调查问卷列表，要么是全员可见，要么是被推送用户
        PropertyWrapper wrapper = new PropertyWrapper(Investigate.class);
        if(template==null){
            template=new Investigate();
            template.setStatus("21");
        }
        wrapper.like(!ObjectUtils.isEmpty(template.getTitle()),"title",template.getTitle());
        wrapper.eq(!ObjectUtils.isEmpty(template.getStatus()),"status",template.getStatus());//已发布的！
        wrapper.eq(!ObjectUtils.isEmpty(template.getSiteid()),"siteid",template.getSiteid());
        wrapper.eq("docstatus",0);
        wrapper.andNew();
        wrapper.eq("pushtorange","全部用户");//全体用户可见
        if(investigateUsers!=null&&investigateUsers.size()>0){
            wrapper.or();
            List<Long> ids=investigateUsers.stream().map(item->item.getInvestigateid()).collect(Collectors.toList());
            wrapper.in("id",ids);//或者：当前用户是被推送目标
        }
        List<Investigate> investigates = investigateMapper.selectList(wrapper.getWrapper());//已获取到当前用户可见的调查问卷

        if(!ObjectUtils.isEmpty(investigates)){
            List<Long> ids = investigates.stream().map(item -> item.getId()).collect(Collectors.toList());
            //获取问卷的题目数
            Map<Long,Integer> mapCountTopic =new HashMap<>();//key为问卷id，value为该问卷总题目数
            List<Map> countTopicAllList=surveyMapper.countTopicByInvest(ids);
            if(countTopicAllList!=null){
                for(Map map:countTopicAllList){
                    if(map.get("ID")!=null){
                        Long id=((Number)map.get("ID")).longValue();
                        int counts=map.get("COUNTS")==null?0:((Number)map.get("COUNTS")).intValue();
                        mapCountTopic.put(id,counts);
                    }
                }
            }

            for(Investigate investigate:investigates){
                Long id=investigate.getId();
                InvestigateCurrentVo vo= (InvestigateCurrentVo) ReflactUtil.obj2obj(investigate,InvestigateCurrentVo.class,true);
                vo.setTotal(mapCountTopic.get(id));
                InvestigateUser investigateUser = mapInvestigateUser.get(id);
                if(investigateUser!=null){
                    vo.setAnswered(investigateUser.getAnswerednum().intValue());
                    vo.setIsCommit(investigateUser.getRecoverstatus().intValue());
                    vo.setSecondRest(investigateUser.getSecondrest());
                    vo.setDtLastAnswer(investigateUser.getModifyTime());
                }else {
                    vo.setAnswered(0);
                    vo.setIsCommit(0);
                    vo.setDtLastAnswer(null);
                }
                vos.add(vo);
            }
        }
        return vos;
    }

    @Override
    public Pager<InvestigateWithCount> pageSurvey(BaseVo<InvestigateQueryVo> vo) {
        Pager<InvestigateWithCount> result= new Pager<>();
        result.setSize(vo.getPager().getSize());
        result.setCurrent(vo.getPager().getCurrent());

        PropertyWrapper wrapper = new PropertyWrapper(Investigate.class);
        InvestigateQueryVo entity = vo.getEntity();
        wrapper.eq(!ObjectUtils.isEmpty(entity.getSiteid()),"siteid",entity.getSiteid());
        wrapper.eq(!ObjectUtils.isEmpty(entity.getStatus()),"status",entity.getStatus());
        wrapper.like(!ObjectUtils.isEmpty(entity.getTitle()),"title",entity.getTitle());
        List<Date> createTime = entity.getCreateTime();
        if(createTime!=null&&createTime.size()==2){
            wrapper.between("createTime",createTime.get(0),createTime.get(1));
        }
        List<Pair<String,Boolean>> sortProps = vo.getPager().getSortProps();
        if(sortProps!=null&&sortProps.size()>0){
            wrapper.orderBy(sortProps);
        }
        Integer total=investigateMapper.selectCount(wrapper.getWrapper());
        result.setTotal(total);

        Page page = new Page(vo.getPager().getCurrent(),vo.getPager().getSize());
        List<Investigate> investigates=investigateMapper.selectPage((IPage<Investigate>) page,wrapper.getWrapper()).getRecords();
        if(investigates!=null&&investigates.size()>0){
            Map<String,Integer> mapCount=new HashMap<>();
            List<Long> ids = investigates.stream().map(item -> item.getId()).collect(Collectors.toList());
            List<Map> counts = surveyMapper.countByRecoverstatus(ids);
            if(counts!=null){
                for(Map count:counts){
                    mapCount.put(count.get("INVESTIGATEID")+"_"+count.get("RECOVERSTATUS"),((Number)count.get("COUNTS")).intValue());
                }
            }
            List<InvestigateWithCount> records= new ArrayList<>();
            for(Investigate investigate:investigates){
                InvestigateWithCount record= (InvestigateWithCount) ReflactUtil.obj2obj(investigate,InvestigateWithCount.class,true);
                Integer countHs=mapCount.get(investigate.getId()+"_1");//回收数
                if(countHs==null){
                    countHs=0;
                }
                Integer countWhs=mapCount.get(investigate.getId()+"_0");//未回收数
                if(countWhs==null){
                    countWhs=0;
                }
                record.setCountPush(String.valueOf(countHs+countWhs));
                record.setCountHs(String.valueOf(countHs));
                records.add(record);
            }
            result.setRecords(records);
        }
        return result;
    }

    @Override
    public List<Map> countRecoverByGroup(Long investigateId) {
        return surveyMapper.countRecoverByGroup(investigateId);
    }

    @Override
    public Pager<InvestigateUser> pageInvestigateUser(BaseVo<InvestigateUser> vo) {
        Pager<InvestigateUser> result= new Pager<>();
        result.setCurrent(vo.getPager().getCurrent());
        result.setSize(vo.getPager().getSize());

        PropertyWrapper wrapper = new PropertyWrapper(InvestigateUser.class);
        InvestigateUser entity = vo.getEntity();
        if(ObjectUtils.isEmpty(entity.getInvestigateid())){
            throw new RuntimeException("请输入问卷id：investigateid");
        }
        wrapper.eq("investigateid",entity.getInvestigateid());
        wrapper.eq(!ObjectUtils.isEmpty(entity.getGroupid()),"groupid",entity.getGroupid());
        wrapper.eq(!ObjectUtils.isEmpty(entity.getRecoverstatus()),"recoverstatus",entity.getRecoverstatus());

        Integer total =investigateUserMapper.selectCount(wrapper.getWrapper());
        result.setTotal(total);

        Page page= new Page(vo.getPager().getCurrent(),vo.getPager().getSize());
        List<InvestigateUser> records = investigateUserMapper.selectPage((IPage<InvestigateUser>) page, wrapper.getWrapper()).getRecords();
        result.setRecords(records);
        return result;
    }

    @Override
    public Map countByAnswer(Long investigateId) {
        Map result= new HashMap<>();
        List<Map> maps = surveyMapper.countByAnswer(investigateId);
        if(maps!=null){
            for(Map map:maps){
                if(!ObjectUtils.isEmpty(map.get("ANSWERCHOOSE"))){
                    result.put(String.valueOf(map.get("ANSWERCHOOSE")),String.valueOf(map.get("COUNTS")));
                }
            }
        }
        return result;
    }

    @Override
    public void exportAnswer(AnswerUser vo, HttpServletResponse response) {
        PropertyWrapper wrapper = new PropertyWrapper(AnswerUser.class);
        wrapper.eq(!ObjectUtils.isEmpty(vo.getCreateBy()),"createBy",vo.getCreateBy());
        wrapper.eq(!ObjectUtils.isEmpty(vo.getInvestigateid()),"investigateid",vo.getInvestigateid());
        wrapper.eq(!ObjectUtils.isEmpty(vo.getTopicid()),"topicid",vo.getTopicid());
        wrapper.eq(!ObjectUtils.isEmpty(vo.getAnswerchoose()),"answerchoose",vo.getAnswerchoose());

        List<AnswerUser> answerUsers = answerUserMapper.selectList(wrapper.getWrapper());
        Set<Long> topicIds=new HashSet<>();
        Set<Long> answerIds = new HashSet<>();
        Set<Long> userIds = new HashSet<>();
        Set<String> answerSet=new HashSet<>();
        for(AnswerUser answerUser:answerUsers){
            topicIds.add(answerUser.getTopicid());
            answerIds.add(answerUser.getAnswerchoose());
            userIds.add(answerUser.getCreateBy());

            String key=answerUser.getCreateBy()+"_"+answerUser.getAnswerchoose();
            answerSet.add(key);
        }
        List<Topic> topics=topicMapper.selectBatchIds(topicIds);
        List<Answer> answers=answerMapper.selectBatchIds(answerIds);
        List<User> users=userMapper.selectBatchIds(userIds);

        List datalist=new ArrayList();
        for(Topic topic:topics){
            for(User user:users){
                for(Answer answer:answers){
                    Map data=new HashMap();
                    data.put("topic",topic.getTitle());
                    data.put("user",user.getTrueName());
                    data.put("answer",answer.getTitle());
                    String key=user.getId()+"_"+answer.getId();
                    int selected=answerSet.contains(key)?1:0;
                    data.put("selected",selected);
                    datalist.add(data);
                }
            }
        }
        List<ExcelBean> excel = new ArrayList<>();
        Map<Integer, List<ExcelBean>> headers = new LinkedHashMap<>();
        excel.add(new ExcelBean("题目","topic",  0));
        excel.add(new ExcelBean("答题者","user",  0));
        excel.add(new ExcelBean("选项", "answer",0));
        excel.add(new ExcelBean("选中=1","selected",  0));
        headers.put(0, excel);
        String sheetName = "answers";  //sheet名
        String excelName = sheetName + ".xlsx";
        try {
            XSSFWorkbook workbook = ExcelUtil.createExcelFile(datalist, headers, sheetName);
            response.reset();
            response.setHeader("content-disposition", "attachement;fileName=answers.xlsx");
            response.setContentType("application/octet-stream");
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportInvestigate(Long investigateid, HttpServletResponse response) {
        Investigate investigate=investigateMapper.selectById(investigateid);
        List<Topic> topics = topicMapper.selectList(new PropertyWrapper<>(Topic.class).eq("investigateid",investigateid).getWrapper());
        List<Answer> answers=answerMapper.selectList(new PropertyWrapper<>(Answer.class).eq("investigateid",investigateid).getWrapper());
        Map<Long,List<Answer>> answerMap = new HashMap<>();
        for(Answer answer:answers){
            Long key=answer.getTopicid();
            List<Answer> values;
            if(answerMap.containsKey(key)){
                values=answerMap.get(key);
            }else {
                values=new ArrayList<>();
                answerMap.put(key,values);
            }
            values.add(answer);
        }
        String sheetName=investigate.getTitle();
        String[] title=new String[]{"题目","答案"};
        List<List<String>> lists =new ArrayList<>();
        for(Topic topic:topics){
            List<String> listTop=new ArrayList();
            listTop.add(topic.getTitle());
            listTop.add("");
            lists.add(listTop);
            for(Answer answer:answerMap.get(topic.getId())){
                List<String> listAnswer= new ArrayList<>();
                listAnswer.add("");
                listAnswer.add(answer.getTitle());
                lists.add(listAnswer);
            }
        }
        export(response,lists,title,sheetName);
    }

    public void export(HttpServletResponse response, List<List<String>> List, String[] title, String sheetName) {
        HSSFWorkbook wb=null;
        try {
            wb=new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet(sheetName);

            HSSFRow titleRow = sheet.createRow(0);

            HSSFCellStyle style=wb.createCellStyle();

            //设置标题
            for (int i=0;i<title.length;i++){
                sheet.setColumnWidth(i,512*10);
                HSSFCell cell = titleRow.createCell(i);
                cell.setCellStyle(style);
                cell.setCellValue(title[i]);
            }

            //填充数据
            for (int i = 1; i < List.size()+1; i++) {
                HSSFRow row = sheet.createRow(i);
                for (int j = 0; j < title.length; j++) {
                    row.createCell(j).setCellValue(List.get(i-1).get(j));
                }
            }
            String fileName=sheetName+".xls";
            response.reset();
            response.setHeader("content-disposition", "attachement;fileName=" + (new String(fileName.getBytes(), "ISO-8859-1")));
            response.setContentType("application/octet-stream");
            wb.write(response.getOutputStream());
            wb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
