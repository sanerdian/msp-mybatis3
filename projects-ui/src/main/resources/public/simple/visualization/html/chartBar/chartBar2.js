$(function () {    
    /*
    * 获取表格和图表数据
    * @param tableFields 表格字段
    */
    function getChartListData(){
        //表格字段
        var tableFields = [
            {"title":"标题","field":"title"},
            {"title":"新增线索数","field":"addNum"},
            {"title":"已转化线索数","field":"changeNum"},
            {"title":"线索转化率","field":"changeRate"}
        ];
        var jsonData = {
            "entity": {},
            "pager": {
                "current": 1,
                "size": 3
            }
        }
        ajax('', '/tonghang/chartshow/listing', 'post', JSON.stringify(jsonData)).then(function (data) {
            if (data.success) {
                setTableData('#chartBarTable', data.obj, tableFields); //生成表格数据
                chartProcessData(data.obj.records); //图表数据处理
                // $('#chartBarTable').remove(); 不生成的html删除
            }
        })
    }

    /*
    * 图表数据处理
    * @param dataArr 元数据表的数据
    */
    function chartProcessData(dataArr){            
        var titleValArr = []; //存放每个字段所有数量的值
        var addNumValArr = [];
        var changeNumValArr = [];
        var changeRateValArr = [];
        for(var k = 0; k < dataArr.length; k++){
            var titleValObj = dataArr[k].title; //每个字段值
            titleValArr.push(titleValObj);
            var addNumValObj = dataArr[k].addNum;
            addNumValArr.push(addNumValObj);
            var changeNumValObj = dataArr[k].changeNum;
            changeNumValArr.push(changeNumValObj);
            var changeRateValObj = dataArr[k].changeRate;
            changeRateValArr.push(changeRateValObj);
        }
         //图表数据
        var chartDataObj = {
            "标题":titleValArr,
            "新增线索数":addNumValArr,
            "已转化线索数":changeNumValArr,
            "线索转化率":changeRateValArr,
        };
        // console.log('chartDataObj',chartDataObj);

        //图表系列类型
        var seriesType = {
            "新增线索数":"bar",
            "已转化线索数":"bar",
            "线索转化率":"line"
        };
        var filedsArr =  ["新增线索数","已转化线索数","线索转化率"]; //字段列表
        var chartSeriesObj = {}; //图表系列数据
        var chartSeriesData = []; //图表系列数据
        for(key in chartDataObj){
            for(key2 in seriesType){
                if(key == key2){
                    if(seriesType[key2] == "bar"){
                        chartSeriesObj = {//type为bar
                            name: key,
                            type: "bar",
                            data: chartDataObj[key],
                            markPoint: {
                                data: [
                                    { type: "max", name: "Max" },
                                    { type: "min", name: "Min" },
                                ],
                            },
                            markLine: {
                                data: [{ type: "average", name: "Avg" }],
                            }
                        }
                    }else if(seriesType[key2] == "line"){
                        chartSeriesObj = {//type为line
                            name: key,
                            type: "line",
                            data: chartDataObj[key],
                            tooltip: {
                                valueFormatter: function (value) {
                                    return value + " %";
                                },
                            }
                        }
                    }
                    chartSeriesData.push(chartSeriesObj);
                }
            }
            // console.log(key,chartDataObj[key]);
            var titleArr = chartDataObj['标题']; //标题数据
        }
        // console.log('chartSeriesData',chartSeriesData);
        createChart(filedsArr, titleArr, chartSeriesData); //创建图表
    }

    /*
    * 创建图表
    * @param legendFiledsArr 图例字段数据
    * @param xAxisTitleArr x轴标题数据
    * @param chartSeriesData 图表系列数据
    */
    function createChart(legendFiledsArr, xAxisTitleArr, chartSeriesData) {
        var dom = document.getElementById("chartBar");
        var myChart = echarts.init(dom, null, {
            renderer: "canvas",
            useDirtyRect: false,
        });
        var option = {
            tooltip: {
                trigger: "axis",
            },
            legend: {
                // data: ["新增线索", "已转化线索", "线索转化率"],
                data: legendFiledsArr,
                bottom: 0,
                textStyle: {
                    fontSize: 11,
                },
            },
            color: ['#5E8CFF', '#5BCEBA', '#fac858'],
            toolbox: {
                show: true,
                feature: {
                    saveAsImage: { show: true, title: "保存为图片" },
                },
                right: "10%",
            },
            // 区域缩放轴
            dataZoom: [
                {
                    type: "inside",
                    filterMode: "filter",
                    height: 15,
                },
                {
                    type: "slider",
                    height: 15,
                },
            ],
            calculable: true,
            xAxis: [
                {
                    type: "category",
                    // data: ['内部自拓', '名片', '外部介绍', '官网', '展会'],
                    data: xAxisTitleArr,
                },
            ],
            yAxis: [
                {
                    type: "value",
                    name: "(个)",
                },
                {
                    type: "value",
                    name: "(%)",
                    axisLabel: {
                        formatter: "{value} %",
                    },
                },
            ],
            series: chartSeriesData
        };
        if (option && typeof option === "object") {
            myChart.setOption(option);
        }
        window.addEventListener("resize", myChart.resize);
    }

    getChartListData();
})