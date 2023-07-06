$(function () {
    function createChartLine(dom) {
        if(dom){
            var myChart = echarts.init(dom, null, {
                renderer: "canvas",
                useDirtyRect: false,
            });
            var option = {
                tooltip: {
                    trigger: "axis",
                },
                legend: {
                    data: ['新增线索', '线索转化率'],
                    bottom: 0,
                    textStyle: {
                        fontSize: 11,
                    },
                },
                color: ['#5E8CFF', '#5BCEBA'],
                toolbox: {
                    show: true,
                    feature: {
                        saveAsImage: { show: true, title: "保存为图片" },
                    },
                    right: "10%",
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: ['内部自拓', '名片', '外部介绍', '官网', '展会'],
                },
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
                series: [
                    {
                        name: '新增线索',
                        type: 'line',
                        // stack: 'Total', //堆叠
                        // areaStyle: {}, //面积
                        // emphasis: {
                        //     focus: 'series'
                        // },
                        data: [2, 2, 2, 0, 1],
                    },
                    {
                        name: '线索转化率',
                        type: 'line',
                        // stack: 'Total', //堆叠
                        // areaStyle: {}, //面积
                        // emphasis: {
                        //     focus: 'series'
                        // },
                        data: [0.00, 0.50, 0.00, 0.50, 2.50],
                    }
                ]
            };
            if (option && typeof option === "object") {
                myChart.setOption(option);
            }
            window.addEventListener("resize", myChart.resize);
        }
    }
       
    function chartLineDom(){
        var elements = document.getElementsByClassName("visual_chart_line");
        if(elements){
            for(var i = 0; i < elements.length; i++){
                var elem = document.querySelectorAll(".visual_chart_line")[i];            
                createChartLine(elem);
            }
        }
    }
    chartLineDom();
})