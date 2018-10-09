
var renderer, scene, camera;
var containerWidth, containerHeight;
var euipmentObj = {};
var errEquipments = [];

function init() {
	$('#mapBox').height($(document).height()-260);
	containerWidth = $('#mapBox').width();
	containerHeight = $('#mapBox').height();
	
	var container = document.getElementById( 'container' );

	scene = new THREE.Scene();
	scene.background = new THREE.Color( 0xb0b0b0 );
	scene.background = new THREE.Color( 0xffffff );

	camera = new THREE.PerspectiveCamera( 50, containerWidth / containerHeight, 1, 1000 );
	camera.position.set( 0, 0, 500 );

//	var group = new THREE.Group();
//	scene.add( group );
//
//	var helper = new THREE.GridHelper( 400, 40 );
//	helper.rotation.x = Math.PI / 2;
//	group.add( helper );
	
	for(var i=0; i<planShapes.length; i++) {
		drawPlanShape(planShapes[i]);
	}
	
	for(var i=0; i<equipments.length; i++) {
		drawEquipment(equipments[i]);
	}

	
	var directionalLight = new THREE.DirectionalLight( 0xffffff, 0.8 );
	directionalLight.position.set( 0.75, 0.75, 1.0 ).normalize();
	scene.add( directionalLight );

	var ambientLight = new THREE.AmbientLight( 0xcccccc, 0.45 );
	scene.add( ambientLight );
	
	

	renderer = new THREE.WebGLRenderer( { antialias: true } );
	renderer.setPixelRatio( window.devicePixelRatio );
	renderer.setSize(containerWidth, containerHeight);
	container.appendChild( renderer.domElement );

	var controls = new THREE.OrbitControls( camera, renderer.domElement );

	$('#mapBox').resize(function(){
		onWindowResize();
	});
	
	
//	windowAddMouseWheel();
//	addTouchListener();

}

function onWindowResize() {
	containerWidth = $('#mapBox').width();
	containerHeight = $('#mapBox').height();

	camera.aspect = containerWidth / containerHeight;
	camera.updateProjectionMatrix();

	renderer.setSize(containerWidth, containerHeight);

}

function animate() {

	requestAnimationFrame( animate );

	render();

}

var colorR = 0;
var colorDir = 1;
function render() {

	renderer.render( scene, camera );
	
//	euipmentObj[equipments[6].id].material = new THREE.MeshLambertMaterial( { color: euipmentObj[equipments[6].id].color0+colorR, overdraw: 0.5 } );
//	colorR+=1000*colorDir;
	if(errEquipments.length>0) {
		for(var i=0; i<errEquipments.length; i++) {
			errEquipments[i].material = new THREE.MeshLambertMaterial( { color: errEquipments[i].color0+colorR, overdraw: 0.5 } );
		}
		colorR+=1000*colorDir;
	}

}

function drawPlanShape(planShape) {
	
	var shapePaths = $.parseJSON(planShape.shapePaths);
	
	if(shapePaths!=null && shapePaths.length>0) {
		var material;
		if(planShape.transparent) {
			material = new THREE.MeshLambertMaterial( { color: planShape.color, overdraw: planShape.overdraw, opacity:planShape.opacity, transparent:true } );
		} else {
			material = new THREE.MeshLambertMaterial( { color: planShape.color, overdraw: planShape.overdraw } );
		}
		 
		var shape = new THREE.Shape();
		
		shape.moveTo(shapePaths[0][0], shapePaths[0][1], shapePaths[0][2]);
		for(var i=1; i<shapePaths.length; i++) {
			shape.lineTo(shapePaths[i][0], shapePaths[i][1], shapePaths[i][2]);
		}
		var extrudeSettings = { amount: planShape.thickness, bevelEnabled: false};
		var extrude = new THREE.Mesh(new THREE.ExtrudeGeometry(shape, extrudeSettings), material);
		extrude.type=planShape.shapeType;
		extrude.isEquipment=false;
		scene.add(extrude);
	}
	
	
	if(planShape.showText) {
		material = new THREE.MeshLambertMaterial( { color: planShape.textColor, overdraw: planShape.textOverdraw } );
		var loader = new THREE.FontLoader();
		loader.load(planShape.textFamily, function(font) {
		  var mesh = new THREE.Mesh(new THREE.TextGeometry(planShape.text, {
			font: font,
			size: planShape.textSize,
			height: planShape.textThickness
		  }), material);
			mesh.position.x = planShape.textX;
			mesh.position.y = planShape.textY;
			mesh.position.z = planShape.textZ;
			mesh.type='text';
		  scene.add(mesh);
		}); 
	}
	
}

var equipmentStyle = {'自助借还机':0x748bed, '检索机':0xed7474, '上网机':0xcc74ed, '门禁':0xff764c, '智能书架控制器':0xff9f7f};
function drawEquipment(equipment) {
	var geometry = new THREE.BoxGeometry( equipment.planWidth, equipment.planHeight, equipment.planThickness );
	var material = new THREE.MeshLambertMaterial( { color: equipmentStyle[equipment.type], overdraw: 0.5 } );
	cube = new THREE.Mesh( geometry, material );
	cube.position.z=equipment.planZ;
	cube.position.x=equipment.planX;
	cube.position.y=equipment.planY;
	cube.equipment = equipment;
	cube.color0 = equipmentStyle[equipment.type];
	euipmentObj[equipment.id] = cube;
	
	if(equipment.hasProblem) {
		errEquipments.push(cube);
	}
	
	scene.add( cube );
}


var raycaster = new THREE.Raycaster();//光线投射，用于确定鼠标点击位置 
var mouse = new THREE.Vector2();//创建二维平面 
document.addEventListener("mousedown",mousedown);//页面绑定鼠标点击事件 
//点击方法 
function mousedown(e){ 
//	//将html坐标系转化为webgl坐标系，并确定鼠标点击位置 
//	mouse.x = e.clientX / renderer.domElement.clientWidth*2-1; 
//	mouse.y = -(e.clientY / renderer.domElement.clientHeight*2)+1; 
	
//	mouse.x = (event.clientX / window.innerWidth) * 2 - 1;  
//	mouse.y = -(event.clientY / window.innerHeight) * 2 + 1; 
	if(e.button!=0) 
		return;
	
	mouse.x = ((event.clientX-$('#container').offset().left) / $('#container').width()) * 2 - 1;  
	mouse.y = -((event.clientY-$('#container').offset().top) / $('#container').height()) * 2 + 1; 
	
	//以camera为z坐标，确定所点击物体的3D空间位置 
	raycaster.setFromCamera(mouse,camera); //确定所点击位置上的物体数量 
	var intersects = raycaster.intersectObjects(scene.children); //选中后进行的操作
	//alert(intersects.length);
	if(intersects.length){ 
		for(var i=0; i<intersects.length; i++) {
			//scene.remove(intersects[i].object);
			if(intersects[i].object.equipment!=null) {
				openEquipment(intersects[i].object.equipment);
				return;
			}
				
		}
	} 
}

///** 
// * 鼠标滚轮前进雨后退 
// */  
//function windowAddMouseWheel() {  
//    var scrollFunc = function (e) {  
//        var positionZ=0;  
//        e = e || window.event;  
//        if (e.wheelDelta) {  //判断浏览器IE，谷歌滑轮事件  
//       camera.position.z=camera.position.z+e.wheelDelta*0.1;  
//            console.log(e.wheelDelta);  
//        } else if (e.detail) {  //Firefox滑轮事件  
//            camera.position.z=camera.position.z+e.detail*0.1;  
//            console.log(e.detail);  
//        }  
//        if(positionZ<=1000&&positionZ>=0.1){  
//            camera.position.z=positionZ;  
//        }  
//    };  
//    //给页面绑定滑轮滚动事件  
//    if (document.addEventListener) {  
//        document.addEventListener('DOMMouseScroll', scrollFunc, false);  
//    }  
////滚动滑轮触发scrollFunc方法  
//    window.onmousewheel = document.onmousewheel = scrollFunc;  
//}
//
///** 
// * 上下左右移动相机 
// */  
//function addTouchListener() {  
//    var startX,endX,startY,endY;  
//    document.body.onmousedown=function (event) {  
//        startX = event.clientX;  
//        startY = event.clientY;  
//  
//    };  
//    document.body.onmousemove=function (event) {  
//        if (event.button == 1 ) {  
//            endX = event.clientX;  
//            endY = event.clientY;  
//            var x=endX-startX;  
//            var y=endY-startY;  
//            if (Math.abs(x)>Math.abs(y)) {  
//                camera.position.x=camera.position.x-x*0.05;  
//            } else {  
//                camera.position.y=camera.position.y+y*0.05;  
//            }  
//            startX=endX;  
//            startY=endY;  
//        }  
//    };  
//}  