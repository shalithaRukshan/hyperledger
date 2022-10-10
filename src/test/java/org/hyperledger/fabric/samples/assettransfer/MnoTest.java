package org.hyperledger.fabric.samples.assettransfer;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MnoTest {

    @Nested
    class Equality {

        @Test
        public void isReflexive() {
            Mno mno = new Mno("121212","mno1", "http://localhost:9099/api/mno");

            assertThat(mno).isEqualTo(mno);
        }

        @Test
        public void isSymmetric() {

            Mno mnoA = new Mno("121212","mno1", "http://localhost:9099/api/mno");
            Mno mnoB = new Mno("121212","mno1", "http://localhost:9099/api/mno");

            assertThat(mnoB).isEqualTo(mnoA);
            assertThat(mnoA).isEqualTo(mnoB);
        }

        @Test
        public void isTransitive() {

            Mno mnoA = new Mno("121212","mno1", "http://localhost:9099/api/mno");
            Mno mnoB = new Mno("121212","mno1", "http://localhost:9099/api/mno");
            Mno mnoC = new Mno("121212","mno1", "http://localhost:9099/api/mno");

            assertThat(mnoA).isEqualTo(mnoB);
            assertThat(mnoB).isEqualTo(mnoC);
            assertThat(mnoC).isEqualTo(mnoA);
        }

        @Test
        public void handlesInequality() {

            Mno mnoA = new Mno("121212","mno1", "http://localhost:9099/api/mno");
            Mno mnoB = new Mno("12134312","mno2", "http://localhost:9099/api/mno");

            assertThat(mnoA).isNotEqualTo(mnoB);
        }

        @Test
        public void handlesOtherObjects() {
            Mno mnoA = new Mno("121212","mno1", "http://localhost:9099/api/mno");
            String assetB = "not a mno";

            assertThat(mnoA).isNotEqualTo(assetB);
        }

        @Test
        public void handlesNull() {
            Mno mnoA = new Mno("121212","mno1", "http://localhost:9099/api/mno");

            assertThat(mnoA).isNotEqualTo(null);
        }
    }

    @Test
    public void toStringIdentifiesAsset() {
        Mno mnoA = new Mno("121212","mno1", "http://localhost:9099/api/mno");
        assertThat(mnoA.toString()).isEqualTo("Mno@fd121b99 [mnoId=121212, mnoName=mno1, endpoint=http://localhost:9099/api/mno]");
    }

}
