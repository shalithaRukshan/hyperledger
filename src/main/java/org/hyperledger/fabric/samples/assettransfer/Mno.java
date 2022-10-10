package org.hyperledger.fabric.samples.assettransfer;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType()
public final class Mno {
    @Property()
    private final String mnoId;

    @Property()
    private final String mnoName;

    @Property()
    private final String endpoint;

    public String getMnoId() {
        return mnoId;
    }

    public String getMnoName() {
        return mnoName;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public Mno(@JsonProperty("mnoId") final String mnoId, @JsonProperty("mnoName") final String mnoName,
               @JsonProperty("endpoint") final String endpoint) {
        this.mnoId = mnoId;
        this.mnoName = mnoName;
        this.endpoint = endpoint;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        Mno other = (Mno) obj;

        return Objects.deepEquals(
                new String[] {getMnoId(), getMnoName(), getEndpoint()},
                new String[] {other.getMnoId(), other.getMnoName(), other.getEndpoint()});
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMnoId(), getMnoName(), getEndpoint());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [mnoId=" + mnoId + ", mnoName="
                + mnoName + ", endpoint=" + endpoint + "]";
    }

}
