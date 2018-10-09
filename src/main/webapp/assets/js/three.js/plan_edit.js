
var renderer, scene, camera, controls;
var containerWidth, containerHeight;
var editableCube;
var objects = [];

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

	var group = new THREE.Group();
	scene.add( group );

	var helper = new THREE.GridHelper( 400, 40 );
	helper.rotation.x = Math.PI / 2;
	group.add( helper );
	
	for(var i=0; i<planShapes.length; i++) {
		drawPlanShape(planShapes[i]);
	}
	
	for(var i=0; i<equipments.length; i++) {
		drawEquipment(equipments[i]);
	}
	
	drawCurrentEquipment();

	
	var directionalLight = new THREE.DirectionalLight( 0xffffff, 0.8 );
	directionalLight.position.set( 0.75, 0.75, 1.0 ).normalize();
	scene.add( directionalLight );

	var ambientLight = new THREE.AmbientLight( 0xcccccc, 0.45 );
	scene.add( ambientLight );
	
	

	renderer = new THREE.WebGLRenderer( { antialias: true } );
	renderer.setPixelRatio( window.devicePixelRatio );
	renderer.setSize(containerWidth, containerHeight);
	container.appendChild( renderer.domElement );
	
	controls = new THREE.TrackballControls( camera, renderer.domElement );

	controls.rotateSpeed = 1.0;
	controls.zoomSpeed = 1.2;
	controls.panSpeed = 0.8;

	controls.noZoom = false;
	controls.noPan = false;
	controls.noRotate = false;
	controls.noRoll = false;  

	controls.staticMoving = true;
	controls.dynamicDampingFactor = 0.3;

	controls.keys = [ 65, 83, 68 ];

	var dragControls = new THREE.DragControls( objects, camera, renderer.domElement );

	dragControls.addEventListener( 'dragstart', function ( event ) { controls.enabled = false; } );
	dragControls.addEventListener( 'dragend', function ( event ) { 
		controls.enabled = true; 
		editableCube.position.z=currentEquipment.planZ;
		$("#planX").val(editableCube.position.x);
		$("#planY").val(editableCube.position.y);
	} );

	$('#mapBox').resize(function(){
		onWindowResize();
	});
	
	$("#planX, #planY").change(function(){
		editableCube.position.x = $("#planX").val();
		editableCube.position.y = $("#planY").val();
	});
	
	$("#planWidth, #planHeight").change(function(){
		var geometry = new THREE.BoxGeometry( $("#planWidth").val(), $("#planHeight").val(), currentEquipment.planThickness );
		editableCube.geometry = geometry;
	});

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

function render() {
	controls.update();
	renderer.render( scene, camera );
	
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

var equipmentStyle = {'default':{color:0x0000ff,width:7,height:7},'自助借还机':{color:0x748bed,width:7,height:6}, '检索机':{color:0xed7474,width:4,height:6}, '上网机':{color:0xcc74ed,width:4,height:6}, '门禁':{color:0xff764c,width:15,height:4}, '智能书架控制器':{color:0xff9f7f,width:5,height:5}};
function drawEquipment(equipment) {
	if(equipment.id == currentEquipment.id) {
		return;
	}
	
	var geometry = new THREE.BoxGeometry( equipment.planWidth, equipment.planHeight, equipment.planThickness );
	var material = new THREE.MeshLambertMaterial( { color: 0xcdcdcd, overdraw: 0.5 } );
	
	cube = new THREE.Mesh( geometry, material );
	cube.position.z=equipment.planZ;
	cube.position.x=equipment.planX;
	cube.position.y=equipment.planY;
	cube.equipment = equipment;
	
	scene.add( cube );
}

function drawCurrentEquipment() {
	var type = currentEquipment.type;
	if(equipmentStyle[currentEquipment.type]==null) {
		type = "default";
	}
	if(currentEquipment.libraryPlan==null) {
//		currentEquipment.planWidth = equipmentStyle[currentEquipment.type].width;
//		currentEquipment.planHeight = equipmentStyle[currentEquipment.type].height;
		currentEquipment.planWidth = equipmentStyle[type].width;
		currentEquipment.planHeight = equipmentStyle[type].height;
		currentEquipment.planThickness = 10;
		currentEquipment.planZ = 5;
		
		$("#planWidth").val(currentEquipment.planWidth);
		$("#planHeight").val(currentEquipment.planHeight);
	}
	var geometry = new THREE.BoxGeometry( currentEquipment.planWidth, currentEquipment.planHeight, currentEquipment.planThickness );
//	var material = new THREE.MeshLambertMaterial( { color: equipmentStyle[currentEquipment.type].color, overdraw: 0.5 } );
	var material = new THREE.MeshLambertMaterial( { color: equipmentStyle[type].color, overdraw: 0.5 } );
	cube = new THREE.Mesh( geometry, material );
	cube.position.z=currentEquipment.planZ;
	cube.position.x=currentEquipment.planX;
	cube.position.y=currentEquipment.planY;
	cube.equipment = currentEquipment;
	
	editableCube = cube;
	objects.push( cube );
	
	scene.add( cube );
}


var raycaster = new THREE.Raycaster();//光线投射，用于确定鼠标点击位置 
var mouse = new THREE.Vector2();//创建二维平面 
document.addEventListener("mousedown",mousedown);//页面绑定鼠标点击事件 
//点击方法 
function mousedown(e){  
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
				//openEquipment(intersects[i].object.equipment);
				return;
			}
				
		}
	} 
}
