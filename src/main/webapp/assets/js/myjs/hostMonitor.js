//监听div大小变化
(function($, h, c) {
	var a = $([]),
	e = $.resize = $.extend($.resize, {}),
	i,
	k = "setTimeout",
	j = "resize",
	d = j + "-special-event",
	b = "delay",
	f = "throttleWindow";
	e[b] = 250;
	e[f] = true;
	$.event.special[j] = {
		setup: function() {
			if (!e[f] && this[k]) {
				return false;
			}
			var l = $(this);
			a = a.add(l);
			$.data(this, d, {
				w: l.width(),
				h: l.height()
			});
			if (a.length === 1) {
				g();
			}
		},
		teardown: function() {
			if (!e[f] && this[k]) {
				return false;
			}
			var l = $(this);
			a = a.not(l);
			l.removeData(d);
			if (!a.length) {
				clearTimeout(i);
			}
		},
		add: function(l) {
			if (!e[f] && this[k]) {
				return false;
			}
			var n;
			function m(s, o, p) {
				var q = $(this),
				r = $.data(this, d);
				r.w = o !== c ? o: q.width();
				r.h = p !== c ? p: q.height();
				n.apply(this, arguments);
			}
			if ($.isFunction(l)) {
				n = l;
				return m;
			} else {
				n = l.handler;
				l.handler = m;
			}
		}
	};
	function g() {
		i = h[k](function() {
			a.each(function() {
				var n = $(this),
				m = n.width(),
				l = n.height(),
				o = $.data(this, d);
				if (m !== o.w || l !== o.h) {
					n.trigger(j, [o.w = m, o.h = l]);
				}
			});
			g();
		},
		e[b]);
	}
})(jQuery, this);


var cpuOptions;
var memoryOptions;
var diskOptions;
var networkOptions;

var gaugeOpts = {
	  lines: 12, // The number of lines to draw
	  angle: 0.05,//0.15, // The length of each line
	  lineWidth: 0.34, // The line thickness
	  pointer: {
	    length: 0.9, // The radius of the inner circle
	    strokeWidth: 0.035, // The rotation offset
	    color: '#000000' // Fill color
	  },
	  limitMax: 'false',   // If true, the pointer will not go past the end of the gauge
	  colorStart: '#FAD07D',//'#CF0000',   // Colors
	  colorStop: '#FAD07D',//'#DA0D0D',    // just experiment with them
	  strokeColor: '#61A5FF',   // to see which ones work best for you
	  generateGradient: true
};

var cpuGauge;
function initCpuGauge(targetId) {
	if(cpuGauge==null) {
		var target = document.getElementById(targetId); // your canvas element
		cpuGauge = new Gauge(target).setOptions(gaugeOpts); // create sexy gauge!
		cpuGauge.maxValue = 100; // set max gauge value
	}	
}

var memoryGauge;
function initMemoryGauge(targetId) {
	if(memoryGauge==null) {
		var target = document.getElementById(targetId); // your canvas element
		memoryGauge = new Gauge(target).setOptions(gaugeOpts); // create sexy gauge!
		memoryGauge.maxValue = 100; // set max gauge value
	}	
}

var cpuChart;
var memoryChart;
var diskChart;
var networkChart;

function createCpuOptions(times, idles, combineds) {
	return {
		 tooltip : {
	 	        trigger: 'axis',
	 	        axisPointer: {
	 	            type: 'cross',
	 	            label: {
	 	                backgroundColor: '#6a7985'
	 	            }
	 	        }
	 	    },
			legend: {
		        data:['空闲率','占用率']
		    },
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },
			xAxis: {
		        type: 'category',
		        boundaryGap: false,
		        data: times
		    },
		    yAxis: {
		        boundaryGap: [0, '50%'],
		        type: 'value',
		        max: 105
		    },
		    series : [
		        {
		            name:'空闲率',
		            type:'line',
		            smooth:true,
		            symbol: 'none',
		            stack: '总量',
		            areaStyle: {normal: {}},
		            data:idles
		        },
		        {
		            name:'占用率',
		            type:'line',
		            smooth:true,
		            symbol: 'none',
		            stack: '总量',
		            areaStyle: {normal: {}},
		            data:combineds
		        }
		    ]
		};
}

function createMmeoryOptions(times, usedPercents) {
	return {
			 tooltip : {
	 	        trigger: 'axis',
	 	        axisPointer: {
	 	            type: 'cross',
	 	            label: {
	 	                backgroundColor: '#6a7985'
	 	            }
	 	        }
	 	    },
			legend: {
		        data:['使用率']
		    },
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },
			xAxis: {
		        type: 'category',
		        boundaryGap: false,
		        data: times
		    },
		    yAxis: {
		        boundaryGap: [0, '50%'],
		        type: 'value',
		        max: 105
		    },
		    series : [
		        {
		            name:'使用率',
		            type:'line',
		            smooth:true,
		            symbol: 'none',
		            stack: '总量',
		            areaStyle: {normal: {}},
		            itemStyle:{color:'#FFD85C'},
		            data:usedPercents
		        }
		    ]
		};
}

function createNetworkOptions(times, rxBytes, txBytes) {
	return {
		 tooltip : {
 	        trigger: 'axis',
 	        axisPointer: {
 	            type: 'cross',
 	            label: {
 	                backgroundColor: '#6a7985'
 	            }
 	        }
 	    },
		legend: {
	        data:['接收字节数','发送字节数']
	    },
	    grid: {
	        left: '3%',
	        right: '4%',
	        bottom: '3%',
	        containLabel: true
	    },
		xAxis: {
	        type: 'category',
	        boundaryGap: false,
	        data: times
	    },
	    yAxis: {
	        boundaryGap: [0, '50%'],
	        type: 'value'
	    },
	    series : [
	        {
	            name:'接收字节数',
	            type:'line',
	            smooth:true,
	            symbol: 'none',
	            stack: '总量',
	            data:rxBytes
	        },
	        {
	            name:'发送字节数',
	            type:'line',
	            smooth:true,
	            symbol: 'none',
	            stack: '总量',
	            data:txBytes
	        }
	    ]
	};
}

function createDiskOptions(parts, totals, useds) {
	return {
		    legend: {
		        data:['总大小','使用量']
		    },
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },
		    xAxis : [
		        {
		            type : 'category',
		            data : parts
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value'
		        }
		    ],
		    series : [
		        {
		            name:'总大小',
		            type:'bar',
		            data:totals
		        },
		        {
		            name:'使用量',
		            type:'bar',
		            data:useds
		        }
		    ]
		};
}

function launchCpuOptions(times, idles, combineds) {
	cpuOptions = createCpuOptions(times, idles, combineds);
}

function launchMemoryOptions(times, usedPercents) {
	memoryOptions = createMmeoryOptions(times, usedPercents);
}

function launchNetworkOptions(times, rxBytes, txBytes) {
	networkOptions = createNetworkOptions(times, rxBytes, txBytes);
}

function launchDiskOptions(parts, totals, useds) {
	diskOptions = createDiskOptions(parts, totals, useds);
}

function initCpuMonitor(targetId, gaugeTargetId, style, times, idles, combineds) {
	// 基于准备好的dom，初始化echarts实例
	if(cpuChart!=null) {
		cpuChart.dispose();
	}
	cpuChart = echarts.init(document.getElementById(targetId), style);
	
	launchCpuOptions(times, idles, combineds);
    // 使用刚指定的配置项和数据显示图表。
	cpuChart.setOption(cpuOptions);
	
	if(cpuGauge!=null) {
		cpuGauge.set(0);
	} else {
		initCpuGauge(gaugeTargetId);
	}
	
	if(combineds!=null && combineds.length>0) {
		cpuGauge.set(combineds[combineds.length-1]);
	}
}

function initMemoryMonitor(targetId, gaugeTargetId, style, times, usedPercents) {
	if(memoryChart!=null) {
		memoryChart.dispose();
	}
	memoryChart = echarts.init(document.getElementById(targetId), style);
	
	launchMemoryOptions(times, usedPercents);
	memoryChart.setOption(memoryOptions);
	
	if(memoryGauge!=null) {
		memoryGauge.set(0);
	} else {
		initMemoryGauge(gaugeTargetId);
	}
	
	if(usedPercents!=null && usedPercents.length>0) {
		memoryGauge.set(usedPercents[usedPercents.length-1]);
	}
}

function initNetworkMonitor(targetId, style, times, rxBytes, txBytes) {
	if(networkChart!=null) {
		networkChart.dispose();
	}
	networkChart = echarts.init(document.getElementById(targetId), style);
	
	launchNetworkOptions(times, rxBytes, txBytes);
	networkChart.setOption(networkOptions);
	
}

function initDiskMonitor(targetId, style, parts, totals, useds) {
	if(diskChart!=null) {
		diskChart.dispose();
	}
	diskChart = echarts.init(document.getElementById(targetId), style);
	
	launchDiskOptions(parts, totals, useds);
	diskChart.setOption(diskOptions);
	
}

function initInfos(hostInfo) {
	$("#computerName").text(hostInfo.computerName);
	$("#computerFullName").text(hostInfo.computerFullName);
	$("#osDescription").text(hostInfo.osDescription+'('+hostInfo.osArch+')');
	$("#osVersion").text(hostInfo.osVersion);
	$("#osVendor").text(hostInfo.osVendor);
	$("#osName").text(hostInfo.osName);
	
	$("#cpu_model").text(hostInfo.cpuModel+' ('+hostInfo.cpuVendor+')');
	$("#network_name").text(hostInfo.networkName+'('+hostInfo.mac+')');
	$("#network_ip").text(hostInfo.ip+'|'+hostInfo.netmask);
	$("#mem_total").text(byteToGB(hostInfo.memory)+'GB');
	$("#swap_total").text(byteToGB(hostInfo.swap)+'GB');
}

var monitorIp;
function initAll(ip, cpu_chart_id, memory_chart_id, network_chart_id, disk_chart_id, cpu_gauge_id, memory_gauge_id, style) {
	monitorIp = ip;
	$.ajax({url:'hostMonitor/monitor?ip='+monitorIp, success:function(rest){
		initCpuMonitor(cpu_chart_id, cpu_gauge_id, style, rest.cpu.times, rest.cpu.idles, rest.cpu.combineds);
		initMemoryMonitor(memory_chart_id, memory_gauge_id, style, rest.memory.times, rest.memory.usedPercents);
		initNetworkMonitor(network_chart_id, style, rest.network.times, rest.network.rxBytes, rest.network.txBytes);
		initDiskMonitor(disk_chart_id, style, rest.disk.parts, rest.disk.totals, rest.disk.useds);
		initInfos(rest.hostInfo);
		
		monitor() ;
	}});
}
function monitor() {
	$.ajax({url:'api/hostMonitor/monitor?ip='+monitorIp, success:function(rest){
		cpuChart.setOption({
			xAxis: {data: rest.cpu.times},
		    series : [{data:rest.cpu.idles},{data:rest.cpu.combineds}]
		});
		cpuGauge.set(rest.cpu.combineds[rest.cpu.combineds.length-1]);
		$("#cpu_user").text(rest.cpu.last.user);
		$("#cpu_sys").text(rest.cpu.last.sys);
		$("#cpu_wait").text(rest.cpu.last.wait);
		$("#cpu_nice").text(rest.cpu.last.nice);
		
		memoryChart.setOption({
			xAxis: {data: rest.memory.times},
		    series : [{data:rest.memory.usedPercents}]
		});
		memoryGauge.set(rest.memory.usedPercents[rest.memory.usedPercents.length-1]);
		$("#mem_used").text(byteToGB(rest.memory.last.used)+"GB/"+byteToGB(rest.memory.last.free)+"GB");
		$("#swap_used").text(byteToGB(rest.memory.last.swapUsed)+"GB/"+byteToGB(rest.memory.last.swapFree)+"GB");
		
		networkChart.setOption({
			xAxis: {data: rest.network.times},
		    series : [{data:rest.network.rxBytes},{data:rest.network.txBytes}]
		});
		$("#network_packets").text(rest.network.last.rxPackets+"/"+rest.network.last.txPackets);
		$("#network_bytes").text(byteToKB(rest.network.last.rxBytes)+"KB/"+byteToKB(rest.network.last.txBytes)+"KB");
		$("#network_dropped").text(rest.network.last.rxDropped+"/"+rest.network.last.txDropped);
		
		diskChart.setOption({
		    series : [{data:rest.disk.totals},{data:rest.disk.useds}]
		});
		
		setTimeout("monitor()",1000)
	}});
}

function showProcess() {
	if(monitorIp==null) {
		alert("当前没有正在监控的机器");
		return;
	}
	$("#processes-table").jqGrid('clearGridData');  //清空表格
	$("#processes-table").jqGrid('setGridParam',{  // 重新加载数据
		url:'hostMonitor/processes?ip='+monitorIp,  
		datatype:'json'
	}).trigger("reloadGrid");
	

	$("#processModal").modal('show');
	
}

function showService() {
	if(monitorIp==null) {
		alert("当前没有正在监控的机器");
		return;
	}
	$("#service-table").jqGrid('clearGridData');  //清空表格
	$("#service-table").jqGrid('setGridParam',{  // 重新加载数据
		url:'hostMonitor/services?ip='+monitorIp,  
		datatype:'json'
	}).trigger("reloadGrid");
	

	$("#serviceModal").modal('show');
	
}

var hisChart;
function showHistory(){
	if(monitorIp==null) {
		alert("当前没有正在监控的机器");
		return;
	}

	findHisAndErr();

	$("#historyModal").modal('show');
}

function findHisAndErr() {
	findHis();
	findErr();
}

function findHis() {
	var infoType = $("input[name='chartType']:checked").val();

	$.ajax({url:'hostMonitor/hisChart', data:{ip:monitorIp, start:$("#start").val(), end:$("#end").val(), infoType:infoType}, success:function(rest){
		if(hisChart!=null) {
			hisChart.dispose();
		}
		hisChart = echarts.init(document.getElementById("historyChart"), "light");
		if(infoType=='CPUInfo') {
			hisChart.setOption(createCpuOptions(rest.times, rest.idles, rest.combineds));
		} else if(infoType=='MemoryInfo') {
			hisChart.setOption(createMmeoryOptions(rest.times, rest.usedPercents));
		} else if(infoType=='NetworkInfo') {
			hisChart.setOption(createNetworkOptions(rest.times, rest.rxBytes, rest.txBytes));
		} else {
			hisChart.setOption(createDiskOptions(rest.parts, rest.totals, rest.useds));
		}
		
	}});
}

function findErr() {
	$("#error-table").jqGrid('clearGridData');  //清空表格
	$("#error-table").jqGrid('setGridParam',{  // 重新加载数据
		url:'hostMonitor/errors',  
		postData:{ip:monitorIp, start:$("#start").val(), end:$("#end").val()},
		datatype:'json'
	}).trigger("reloadGrid");
}

function byteToGB(byte) {
	var gb = byte/1024/1024/1024;
	gb = Math.round(parseFloat(gb)*100)/100
	return gb;
}

function byteToKB(byte) {
	var gb = byte/1024;
	gb = Math.round(parseFloat(gb)*100)/100
	return gb;
}