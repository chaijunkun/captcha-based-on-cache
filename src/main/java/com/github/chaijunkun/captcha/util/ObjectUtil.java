package com.github.chaijunkun.captcha.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * 对象组件，用于判断各种对象是否为空
 * @author chaijunkun
 */
public class ObjectUtil {

	public static boolean isEmpty(Object obj){
		if(obj == null){  
			return true ;  
		}  

		if(obj.getClass().isArray()){  
			if(Array.getLength(obj) == 0){  
				return true ;  
			}  
		}  

		if(obj instanceof Collection<?>){  
			Collection<?> collection = ( Collection<?>) obj ;  
			if(collection.isEmpty()){  
				return true ;  
			}  
		}  

		if(obj instanceof Map<?,?>){  
			Map<?,?> map = ( Map<?,?> ) obj ;  
			if(map.isEmpty()){  
				return true ;  
			}  
		}  

		return false ;  
	}

	public static boolean isNotEmpty(Object obj){
		return !isEmpty(obj);
	}
}
