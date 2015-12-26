/*
 * Decompiled with CFR 0_110.
 */
package com.google.android.mms.pdu;

import com.google.android.mms.InvalidHeaderValueException;
import com.google.android.mms.pdu.EncodedStringValue;
import com.google.android.mms.pdu.GenericPdu;
import com.google.android.mms.pdu.PduHeaders;

public class DeliveryInd
extends GenericPdu {
    public DeliveryInd() throws InvalidHeaderValueException {
        this.setMessageType(134);
    }

    DeliveryInd(PduHeaders pduHeaders) {
        super(pduHeaders);
    }

    public long getDate() {
        return this.mPduHeaders.getLongInteger(133);
    }

    public byte[] getMessageId() {
        return this.mPduHeaders.getTextString(139);
    }

    public int getStatus() {
        return this.mPduHeaders.getOctet(149);
    }

    public EncodedStringValue[] getTo() {
        return this.mPduHeaders.getEncodedStringValues(151);
    }

    public void setDate(long l) {
        this.mPduHeaders.setLongInteger(l, 133);
    }

    public void setMessageId(byte[] arrby) {
        this.mPduHeaders.setTextString(arrby, 139);
    }

    public void setStatus(int n) throws InvalidHeaderValueException {
        this.mPduHeaders.setOctet(n, 149);
    }

    public void setTo(EncodedStringValue[] arrencodedStringValue) {
        this.mPduHeaders.setEncodedStringValues(arrencodedStringValue, 151);
    }
}
