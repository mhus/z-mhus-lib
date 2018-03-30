/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.errors;

import java.util.UUID;

public class MRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private UUID errorId = UUID.randomUUID();
	
	public MRuntimeException(Object ... in) {
		super(MException.argToString(4,in),MException.argToCause(4,in));
	}
	
	@Override
	public String toString() {
		return errorId.toString() + " " + super.toString();
	}
	
	public UUID getId() {
		return errorId;
	}
	
}
