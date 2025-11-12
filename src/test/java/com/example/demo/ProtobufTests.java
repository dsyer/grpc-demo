/*
 * Copyright 2025-current the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.demo;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.util.JsonFormat;

@EnabledIf("protobufAvailable")
public class ProtobufTests {

	static boolean protobufAvailable() {
		return new File("target/hello.pb").exists();
	}

	@Test
	void json() throws Exception {
		Resource descriptorFile = new DefaultResourceLoader().getResource("file:target/hello.pb");
		DescriptorProtos.FileDescriptorSet fileDescriptorSet = DescriptorProtos.FileDescriptorSet
				.parseFrom(descriptorFile.getInputStream());
		DescriptorProtos.FileDescriptorProto fileProto = fileDescriptorSet.getFile(0);
		Descriptors.FileDescriptor fileDescriptor = Descriptors.FileDescriptor.buildFrom(fileProto, new Descriptors.FileDescriptor[] {});
		Descriptors.Descriptor descriptor = fileDescriptor.findMessageTypeByName("HelloRequest");
		DynamicMessage.Builder builder = DynamicMessage.newBuilder(descriptor);
		
		JsonFormat.parser().merge("{\"name\":\"foo\"}", builder);
		DynamicMessage message = builder.build();
		System.out.println(message);
	}

}
