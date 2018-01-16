package de.mhus.lib.mongo;

import java.util.Date;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.annotations.Property;

import de.mhus.lib.adb.DbComfortableObject;
import de.mhus.lib.basics.UuidIdentificable;

public class MoMetadata extends DbComfortableObject implements UuidIdentificable {

	@Id
	private ObjectId id;
	@Property
	private Date creationDate;
	@Property
	private Date modifyDate;
	@Property
	private long vstamp;

	@Override
	public UUID getId() {
		// https://gist.github.com/enaeseth/5768348
		if (id == null) return null;
		
		long ts = id.getDate().getTime();
		int mid = id.getMachineIdentifier();
		int cnt = id.getCounter();
        byte[] data = new byte[16];

        data[0] = (byte)(ts & 0xff);
        data[1] = (byte)(ts >> 8 & 0xff);
        data[2] = (byte)(ts >> 16 & 0xff);
        data[3] = (byte)(ts >> 24 & 0xff);
        data[4] = (byte)(ts >> 32 & 0xff);
        data[5] = (byte)(ts >> 40 & 0xff);
        data[6] = (byte)(ts >> 48 & 0xff);
        data[7] = (byte)(ts >> 56 & 0xff);

        data[8] = (byte)(cnt & 0xff);
        data[9] = (byte)(cnt >> 8 & 0xff);
        
        data[10] = (byte)(mid & 0xff);
        data[11] = (byte)(mid >> 8 & 0xff);
        data[12] = (byte)(mid >> 16 & 0xff);
        data[13] = (byte)(mid >> 24 & 0xff);
        data[14] = (byte)(mid >> 32 & 0xff);
        data[15] = (byte)(mid >> 40 & 0xff);
        
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
        long mostSigBits = msb;
        long leastSigBits = lsb;

        return new UUID(mostSigBits,leastSigBits);
	}
	
	public ObjectId getObjectId() {
		return id;
	}

	@PrePersist
	public void prePersist() {
		if (creationDate == null)
			creationDate = new Date();
		else
			vstamp += 1;
		modifyDate = new Date();
	}
	
	public Date getCreationDate() {
		return creationDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public long getVstamp() {
		return vstamp;
	}
	
}
