package com.syo.platform.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="t_monitor_planshape")
public class PlanShape {

	@Id
	@GeneratedValue(generator="uuidGen")
	@GenericGenerator(name="uuidGen", strategy="uuid")
	@Column(length=50)
	private String id;
	
	@Column(length=50)
	private String name;
	
	@Column(length=15)
	private String shapeType;
	
	private int sort;

	private int color;
	
	private double overdraw = 0.5;
	
	private boolean transparent;//允许透明
	
	private double opacity;//透明度
	
	private double thickness;//厚度
	
	private boolean bevelEnabled;//是否允许棱角平滑过渡； 
	
	private double bevelSize;//棱角平滑过渡的尺寸
	
	@Lob
	@Basic(fetch=FetchType.LAZY)
	private String shapePaths;
	
	private boolean showText;
	
	@Column(length=50)
	private String text;
	
	private int textColor = 0x999999;
	
	private double textOverdraw = 0.5;
	
	@Column(length=500)
	private String textFamily = "assets/js/three.js/fonts/Microsoft YaHei_Regular.json";
	
	private double textSize = 10;
	
	private double textThickness = 0;
	
	private double textX;
	
	private double textY;
	
	private double textZ;
	
	@ManyToOne
	@JoinColumn(name="lib_plan")
	private LibraryPlan libraryPlan;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public double getOverdraw() {
		return overdraw;
	}

	public void setOverdraw(double overdraw) {
		this.overdraw = overdraw;
	}

	public boolean isTransparent() {
		return transparent;
	}

	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}

	public double getOpacity() {
		return opacity;
	}

	public void setOpacity(double opacity) {
		this.opacity = opacity;
	}

	public double getThickness() {
		return thickness;
	}

	public void setThickness(double thickness) {
		this.thickness = thickness;
	}

	public boolean isBevelEnabled() {
		return bevelEnabled;
	}

	public void setBevelEnabled(boolean bevelEnabled) {
		this.bevelEnabled = bevelEnabled;
	}

	public double getBevelSize() {
		return bevelSize;
	}

	public void setBevelSize(double bevelSize) {
		this.bevelSize = bevelSize;
	}

	public String getShapePaths() {
		return shapePaths;
	}

	public void setShapePaths(String shapePaths) {
		this.shapePaths = shapePaths;
	}

	public boolean isShowText() {
		return showText;
	}

	public void setShowText(boolean showText) {
		this.showText = showText;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public double getTextOverdraw() {
		return textOverdraw;
	}

	public void setTextOverdraw(double textOverdraw) {
		this.textOverdraw = textOverdraw;
	}

	public String getTextFamily() {
		return textFamily;
	}

	public void setTextFamily(String textFamily) {
		this.textFamily = textFamily;
	}

	public double getTextSize() {
		return textSize;
	}

	public void setTextSize(double textSize) {
		this.textSize = textSize;
	}

	public double getTextThickness() {
		return textThickness;
	}

	public void setTextThickness(double textThickness) {
		this.textThickness = textThickness;
	}

	public double getTextX() {
		return textX;
	}

	public void setTextX(double textX) {
		this.textX = textX;
	}

	public double getTextY() {
		return textY;
	}

	public void setTextY(double textY) {
		this.textY = textY;
	}

	public double getTextZ() {
		return textZ;
	}

	public void setTextZ(double textZ) {
		this.textZ = textZ;
	}

	public LibraryPlan getLibraryPlan() {
		return libraryPlan;
	}

	public void setLibraryPlan(LibraryPlan libraryPlan) {
		this.libraryPlan = libraryPlan;
	}

	public String getShapeType() {
		return shapeType;
	}

	public void setShapeType(String shapeType) {
		this.shapeType = shapeType;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
	
	
	
	
	
}
