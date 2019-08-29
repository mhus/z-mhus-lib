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
package de.mhus.lib.mongo;


import java.util.UUID;

import org.bson.types.ObjectId;
import org.codehaus.jackson.map.ObjectMapper;

import com.mongodb.BasicDBObject;


@SuppressWarnings("deprecation")
public class MoUtil {

	static ObjectMapper jsonMapper = new ObjectMapper();

	public static BasicDBObject jsonMarshall(String jsonString) throws Exception {
//	    Writer writer = new StringWriter();
//	    jsonMapper.writer().writeValue(writer, obj);
//	    return (BasicDBObject) BasicDBObject.parse(writer.toString());
		return BasicDBObject.parse(jsonString);
	}

	public static UUID toUUID(ObjectId id) {
		// https://gist.github.com/enaeseth/5768348
		if (id == null) return null;
		
		int ts = id.getTimestamp();
		int mid = id.getMachineIdentifier();
		short pi = id.getProcessIdentifier();
		int cnt = id.getCounter();
        byte[] data = new byte[16];

        data[0] = (byte)(ts & 0xff);
        data[1] = (byte)(ts >> 8 & 0xff);
        data[2] = (byte)(ts >> 16 & 0xff);
        data[3] = (byte)(ts >> 24 & 0xff);
        
        data[4] = (byte)(cnt & 0xff);
        data[5] = (byte)(cnt >> 8 & 0xff);
        data[7] = (byte)(cnt >> 16 & 0xff);
        
        data[6] = 0;
        data[8] = 0;
        
        data[11] = (byte)(mid & 0xff);
        data[12] = (byte)(mid >> 8 & 0xff);
        data[13] = (byte)(mid >> 16 & 0xff);
        data[14] = (byte)(pi & 0xff);
        data[15] = (byte)(pi >> 8 & 0xff);
        
        // prepare UUID data
        data[6]  &= 0x0f;  /* clear version        */
        data[6]  |= 0x40;  /* set to version 4     */
        data[8]  &= 0x3f;  /* clear variant        */
        data[8]  |= 0x80;  /* set to IETF variant  */
        
        long msb = 0;
        long lsb = 0;
        for (int i=0; i<8; i++)
            msb = (msb << 8) | (data[i] & 0xff);
        for (int i=8; i<16; i++)
            lsb = (lsb << 8) | (data[i] & 0xff);

        return new UUID(msb,lsb);
    }
	
	public static ObjectId toObjectId(UUID id) {
		if (id == null) return null;

		long lsb = id.getLeastSignificantBits();
		long msb = id.getMostSignificantBits();
		
		byte[] data = new byte[16];
		
        for (int i=7; i>=0; i--) {
        	data[i] = (byte) (msb & 0xff);
        	msb = msb >> 8;
        }
        for (int i=15; i>=8; i--) {
        	data[i] = (byte) (lsb & 0xff);
        	lsb = lsb >> 8;
        }
        
        int ts = 0;
        int mid = 0;
        int pi = 0;
        int cnt = 0;

        ts = data[3] & 0xff;
        ts = (ts << 8) + (data[2] & 0xff);
        ts = (ts << 8) + (data[1] & 0xff);
        ts = (ts << 8) + (data[0] & 0xff);
        
        cnt = data[7] & 0xff;
        cnt = (cnt << 8) + (data[5] & 0xff);
        cnt = (cnt << 8) + (data[4] & 0xff);
        
        mid = data[13] & 0xff;
        mid = (mid << 8) + (data[12] & 0xff);
        mid = (mid << 8) + (data[11] & 0xff);
        
        pi = data[15] & 0xff;
        pi = (pi << 8) + (data[14] & 0xff);
        
        return new ObjectId(ts, mid, (short)pi, cnt);
		
	}

}
