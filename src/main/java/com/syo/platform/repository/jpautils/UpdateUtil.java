package com.syo.platform.repository.jpautils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.data.jpa.repository.JpaRepository;

import com.syo.platform.entity.Equipment;
import com.syo.platform.repository.EquipmentRepository;

public class UpdateUtil<T> {

	public void update(T newItem, JpaRepository repository, String... ignoreField) throws IllegalArgumentException, IllegalAccessException {
		Class clazz = newItem.getClass();
		Field[] fields = clazz.getDeclaredFields();
		List<String> ignores = Arrays.asList(ignoreField);
		T oldItem = null;
		for(Field field :fields) {
			field.setAccessible(true);
			if(field.getAnnotation(Id.class)!=null) {
				if(field.get(newItem)==null || field.get(newItem).toString().trim().equals("")) {
					return;
				}
				oldItem = (T) repository.findOne((Serializable) field.get(newItem));
				break;
			}
			
		}
		for(Field field :fields) {
			field.setAccessible(true);
			if(field.getAnnotation(Transient.class)!=null) {
				continue;
			}
			if(ignores.contains(field.getName())) {
				continue;
			}
			field.set(oldItem, field.get(newItem));
		}
		repository.save(oldItem);
	}

	
}
