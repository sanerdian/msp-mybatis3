$(function () {
    function createChartBar(dom) {
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
                    data: ["新增线索", "已转化线索", "线索转化率"],
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
                        data: ['内部自拓', '名片', '外部介绍', '官网', '展会'],
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
                series: [
                    {
                        name: "新增线索",
                        type: "bar",
                        data: [2, 2, 2, 0, 1],
                        markPoint: {
                            data: [
                                { type: "max", name: "Max" },
                                { type: "min", name: "Min" },
                            ],
                        },
                        markLine: {
                            data: [{ type: "average", name: "Avg" }],
                        },
                    },
                    {
                        name: "已转化线索",
                        type: "bar",
                        data: [0, 1, 1, 2, 2],
                        markPoint: {
                            data: [
                                { name: "Max", value: 182.2, xAxis: 7, yAxis: 183 },
                                { name: "Min", value: 2.3, xAxis: 11, yAxis: 3 },
                            ],
                        },
                        markLine: {
                            data: [{ type: "average", name: "Avg" }],
                        },
                    },
                    {
                        name: "线索转化率",
                        type: "line",
                        tooltip: {
                            valueFormatter: function (value) {
                                return value + " %";
                            },
                        },
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
    
    function chartBarDom(){
        var elements = document.getElementsByClassName("visual_chart_bar");
        if(elements){
            for(var i = 0; i < elements.length; i++){
                var elem = document.querySelectorAll(".visual_chart_bar")[i];            
                createChartBar(elem);
            }
        }
    }
    chartBarDom();
})