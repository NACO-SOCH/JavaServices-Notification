package gov.naco.soch.notification.service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;


public class JsonObjectParser {

/**
     *  method return convert object to json
     *  @param 	object			Object
     *  @return 				String
     *  @exception JsonGenerationException,JsonMappingException,IOException
*/	
	public static String compose(Object object) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper().configure(MapperFeature.USE_ANNOTATIONS, true);
		Writer strWriter = new StringWriter();
		mapper.writeValue(strWriter, object);
		return strWriter.toString();
	}

/**
     *  method return convert json to Object for specified class
     *  @param 	json			String
     *  @param 	class1			Class Name
     *  @return 				Object
     *  @exception JsonGenerationException,JsonMappingException,IOException
*/		
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object parse(String json, Class class1) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper().configure(MapperFeature.USE_ANNOTATIONS, true);
		return mapper.readValue(json, class1);
	}

/**
     *  method return convert json to Raw type
     *  @param 	json			String
     *  @param 	type			TypeReference
     *  @return 				Object
     *  @exception JsonGenerationException,JsonMappingException,IOException
*/		
	
	@SuppressWarnings("rawtypes")
	public static Object parse(String json, TypeReference type) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, type);
	}

}
