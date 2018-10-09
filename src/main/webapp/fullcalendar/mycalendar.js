var currentBegin = "";
var currentEnd = "";

$(function(){
	//$("#winEventList").width($(document).width()-200);
	//$("#winEventList").height($(document).height()-100);
	//alert($(document).height());
	$("#calendarbox").height($(document).height()-189);
	
	$("#winEventList").window("resize",{width:$(document).width()-100,height:$(document).height()-100});
	$("#winEventList").window("center");
	
	//doClose();
	$("#winEventCard").window("close",true);	
	$("#winEventList").window("close",true);
	//$("#START").datetimebox('setValue',new Date());
	//alert(monent().format(new Date()));
	$('#calendar').fullCalendar({
		defaultView:"agendaWeek",
		header: false,
		defaultDate: new Date(),
		lang: 'zh_cn',
		buttonIcons: false, // show the prev/next text
		weekNumbers: false,
		editable: false,
		firstDay:0,
		contentHeight:$(document).height()-189,
		selectable: true,
		selectHelper: true,
		events:{
			url: 'mycalendar.jsp?command=eventList',
			error: function() {
				alert(11);
			}
		},
		select: function(start, end) {
			openCalendarCard(start, end);
		},
		eventClick:function(calEvent, jsEvent, view) {	
			openCalendarCardByEvt(calEvent);
		},
		viewRender:function(event, element){
			var begin = getDateStr($('#calendar').fullCalendar('getView').start);
			var end = getDateStr($('#calendar').fullCalendar('getView').end);
		}
	});
	$("#dateInfo").html("日期：&nbsp;"+$('#calendar').fullCalendar('getView').title+"&nbsp;");
	
	
	//changeView("agendaWeek");
});

function getDateStr(d) {
	var year = d.year()+"";
	var month = (d.month()+1)<10?("0"+(d.month()+1)):(d.month()+1+"");
	var date = d.date()<10?("0"+d.date()):(d.date()+"");
	var hour = d.hour()<10?("0"+d.hour()):(d.hour()+"");
	var minute = d.minute()<10?("0"+d.minute()):(d.minute()+"");
	return year+"-"+month+"-"+date+" "+hour+":"+minute;
}

function getDateStr2(d) {		
	var year = d.getFullYear()+"";
	var month = (d.getMonth()+1)<10?("0"+(d.getMonth()+1)):(d.getMonth()+1+"");
	var date = d.getDate()<10?("0"+d.getDate()):(d.getDate()+"");
	var hour = d.getHours()<10?("0"+d.getHours()):(d.getHours()+"");
	var minute = d.getMinutes()<10?("0"+d.getMinutes()):(d.getMinutes()+"");
	return year+"-"+month+"-"+date+" "+hour+":"+minute;
}

//改变视图，月：month、周：agendaWeek、日：agendaDay
function changeView(viewName){
	//month,basicWeek,basicDay,agendaWeek,agendaDay
	$("#calendar").fullCalendar('changeView',viewName);
	$("a[btnType='viewBtn']").removeClass("menuSel");
	$("#btn_"+viewName).addClass("menuSel");
	if(viewName=="month"){
		$("#btn_prev").html("上一月&nbsp;");
		$("#btn_next").html("下一月&nbsp;");
	} else if(viewName=="agendaWeek"){
		$("#btn_prev").html("上一周&nbsp;");
		$("#btn_next").html("下一周&nbsp;");
	} else {
		$("#btn_prev").html("上一日&nbsp;");
		$("#btn_next").html("下一日&nbsp;");
	}
	$("#dateInfo").html("日期：&nbsp;"+$('#calendar').fullCalendar('getView').title+"&nbsp;");
}

//转到下一周期
function toNext(){
	$('#calendar').fullCalendar('next');
	$("#dateInfo").html("日期：&nbsp;"+$('#calendar').fullCalendar('getView').title+"&nbsp;");;
}

//转到上一周期
function toPrev(){
	$('#calendar').fullCalendar('prev');	
	$("#dateInfo").html("日期：&nbsp;"+$('#calendar').fullCalendar('getView').title+"&nbsp;");
}

//展开/收起短信设置内容
function showSMSTR(){
	//alert($("#winEventCard").height());
	if($("#NOTICE_EXIST:checked").length>0){
		$("#winEventCard").height(430);
		$(".SMSTR").show();
	} else {
		$(".SMSTR").hide();	
		$("#winEventCard").height(294);
	}
}

//展开/收起参与人员
function showLeaderSpan(){
	if($("input[name='IS_LEADER']:checked").val()=='1'){
		$("#leaderSpan").show();
	} else {
		$("#LEADER_ID").val("");
		$("#LEADER_NAME").val("");
		$("#leaderSpan").hide();	
	}
}

//打开事件编辑卡
function openCalendarCard(start, end){
	$('#calendar').fullCalendar('unselect');
	var startStr = start.year()+"-"+(start.month()+1)+"-"+start.date()+" "+start.hour()+":"+start.minute();
	var endStr = end.year()+"-"+(end.month()+1)+"-"+end.date()+" "+end.hour()+":"+end.minute();
	$("#START").datetimebox('setValue',startStr);
	$("#END").datetimebox('setValue',endStr);
	showLeaderSpan();
	showSMSTR();
	
	$("#winEventCard").window("open",true);
	$("#winEventCard").window("center");
}

var currentEvtObj = null;
//打开事件编辑卡(根据已存在事件)
function openCalendarCardByEvt(calendarEvt){
	//$("#NOTICE_EXIST").attr("checked",false);
	currentEvtObj = calendarEvt;
	for(var f in calendarEvt){
		if($("#"+f+"").length>0) {
			if(f!="NOTICE_EXIST" && f!="START" && f!="END") {
				$("#"+f+"").val(calendarEvt[f]);	
				//alert(f);
			}
		} else {
			$(":radio[name='"+f+"']:eq("+calendarEvt[f]+")").attr("checked",true);	
		}
	}
	$("#NOTICE_EXIST").attr("checked","1"==calendarEvt['NOTICE_EXIST']+"");
	$("#spanDelEvt").show();
	openCalendarCard(calendarEvt.start, calendarEvt.end);	
}

//关闭事件编辑卡前的工作
function closeCalendarCard(){
	$("#NOTICE_EXIST").attr("checked",false);
	$("#START").datetimebox('setValue','');
	$("#END").datetimebox('setValue','');
	$("input:text").val("");
	$("input:hidden").val("");
	$(":checkbox").attr("checked",false);
	$("textarea").val("");
	$(":radio[name='IS_LEADER']:eq(0)").attr("checked","checked");
	$(":radio[name='NOTICE_TARGET']:eq(0)").attr("checked","checked");
	$("#NOTICE_PRETIME_TYPE option:eq(0)").attr("selected",true);
	$("#NOTICE_REPEAT option:eq(0)").attr("selected",true);
	$("#spanDelEvt").hide();
	showLeaderSpan();
	showSMSTR();
	//alert(111);
}
//关闭事件编辑卡
function doClose(){
	$("#winEventCard").window("close",false);	
}

//删除事件
function delCalendarEvent(){
	if(currentEvtObj==null) {
		return;	
	}
	var flag = confirm("确定要删除当前事件？");
	if(!flag) {
		return;	
	}
	$.ajax({
	   type: "POST",
	   url: "mycalendar.jsp",
	   data: {command:"delEvent",CALENDAR_ID:currentEvtObj.CALENDAR_ID},
	   success: function(msg){
			$('#calendar').fullCalendar('refetchEvents');
	   }
	});	
	currentEvtObj = null;
	doClose();
	$("#winEventList").window("close",true);	
}

//添加事件到日程
function addCalendarEvent(){
	var title = $("#TITLE").val();
	var start = $("#START").datetimebox('getValue');
	var end = $("#END").datetimebox('getValue');
	
	if(title==null ||$.trim(title)=='') {
		alert("请输入日程标题");
		rturn;
	}

	if(start>=end) {
		alert("开始时间必须小于结束时间");
		return;	
	}
	
	
	var eventData;
	if (title) {
		eventData = {
			title: title,
			start: start,
			end: end,
			TITLE:title,
			START:start,
			END:end,
			LEADER_ID:$("#LEADER_ID").val(),
			LEADER_NAME:$("#LEADER_NAME").val(),
			CONTENT:$("#CONTENT").val(),
			NOTICE_EXIST:$("#NOTICE_EXIST:checked").length+"",
			IS_LEADER:$(":radio[name='IS_LEADER']:checked").val(),
			NOTICE_TARGET:$(":radio[name='NOTICE_TARGET']:checked").val(),
			NOTICE_PRETIME_TYPE:$("#NOTICE_PRETIME_TYPE").val(),
			NOTICE_REPEAT:$("#NOTICE_REPEAT").val()
		};
		
		var param = {command:"addEvent"};
		if(currentEvtObj!=null) {
			//改为编辑
			param.command = "updateEvent";
			param.CALENDAR_ID = currentEvtObj.CALENDAR_ID;
			currentEvtObj = null;
		}
		for(var f in eventData) {
			if(f!='title' && f!='start' && f!='end') {
				param[f] = eventData[f];	
			}	
		}
		//alert(JSON.stringify(param));
		$.ajax({
		   type: "POST",
		   url: "mycalendar.jsp",
		   data: param,
		   success: function(msg){
				//$('#calendar').fullCalendar('renderEvent', eventData, true);
				$('#calendar').fullCalendar('refetchEvents');
		   }
		});	
	}
	$('#calendar').fullCalendar('unselect');
	doClose();	
	$("#winEventList").window("close",true);
}

function search() {
	if($("#keyword").val()=="请输入关键字" || $.trim($("#keyword").val())=="") {
		return;	
	}
	
	var key = $("#keyword").val();	
	
	var evts = $('#calendar').fullCalendar('clientEvents');
	//alert(evts.length);	
	for(var i=0; i<evts.length; i++) {
		var evt = evts[i];
		//alert(evt.backgroundColor);
		if((evt.TITLE!=null && evt.TITLE.indexOf(key)>=0) || 
			(evt.CONTENT!=null && evt.CONTENT.indexOf(key)>=0)) {	
			evt.className="restEvent";
		}
	}
	$('#calendar').fullCalendar('rerenderEvents');
}

function claerText(){
	if($("#keyword").val()=="请输入关键字") {
		$("#keyword").val("");	
	}
}

var evtMapping = {};
function fetchList() {
	evtMapping = {};
	$('#listBody').html("");
	var evts = $('#calendar').fullCalendar('clientEvents');
	var html = "";
	for(var i=0; i<evts.length; i++) {
		var evt = evts[i];
		evtMapping[evt.CALENDAR_ID] = evt;
		var trClass = 'class="list_dataTR"';
		if(i%2==1){
			trClass = 'class="list_dataTR list_dataTR_odd"';	
		}
		var type = evt.IS_LEADER=='0'?'私人日程':'参与人员';
		var state = evt.START>getDateStr2(new Date())?'<font color="green">未过期</font>':'<font color="red">已过期</font>';
		//var state = '未过期';
		var isSMS = evt.NOTICE_EXIST=='0'?'否':'是';
		
		html += '<tr '+trClass+' onclick="openCalendarCardByEvt(evtMapping[\''+evt.CALENDAR_ID+'\'])">';
			html += '<td align="center"><span>'+(i+1)+'</span></td>';
			html += '<td><span>'+evt.TITLE+'</span></td>';
			html += '<td><span>'+type+'</span></td>';
			html += '<td><span>'+evt.START+'</span></td>';
			html += '<td><span>'+evt.END+'</span></td>';
			html += '<td><span>'+evt.CONTENT+'</span></td>';
			html += '<td><span>'+evt.OWNER_USER+'</span></td>';
			html += '<td><span>'+state+'</span></td>';
			html += '<td><span>'+isSMS+'</span></td>';
		html += '</tr>';
	}
	$('#listBody').html(html);
	
	$("#winEventList").window("open",true);
	$("#winEventList").window("center");
}

function openChoseUser() {
	var height = window.screen.availHeight;
	var width = window.screen.availWidth;
	var top = (height - 670) / 2;
	var left = (width - 1024) / 2;
	var result = window.open("UserSel_calendar.jsp", "选择", "height=670px, width=1024px,top=" + top + ",left=" + left + ", toolbar=no, modal=yes, menubar=no, scrollbars=yes, resizable=yes, location=no");
	return;
}