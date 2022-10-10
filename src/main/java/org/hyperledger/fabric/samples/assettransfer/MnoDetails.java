package org.hyperledger.fabric.samples.assettransfer;

import com.owlike.genson.Genson;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import java.util.ArrayList;
import java.util.List;

@Contract(
        name = "basic1",
        info = @Info(
                title = "Mno details",
                description = "The hyperlegendary asset transfer",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "a.transfer@example.com",
                        name = "Adrian Transfer",
                        url = "https://hyperledger.example.com")))
@Default
public final class MnoDetails implements ContractInterface {

    private final Genson genson = new Genson();

    private enum MnoDetailsErrors {
        MNO_NOT_FOUND,
        MNO_ALREADY_EXIST
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void InitLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        CreateMno(ctx, "mno1", "mno1", "http://localhost:9000/api/mno" );
        CreateMno(ctx, "mno2", "mno2", "http://localhost:9000/api/mno");
        CreateMno(ctx, "mno3", "mno3", "http://localhost:9000/api/mno");
        CreateMno(ctx, "mno4", "mno4", "http://localhost:9000/api/mno");
        CreateMno(ctx, "mno5", "mno5", "http://localhost:9000/api/mno");
        CreateMno(ctx, "mno6", "mno6", "http://localhost:9000/api/mno");

    }

    /**
     * Creates a new MNO on the ledger.
     *
     * @param ctx            the transaction context
     * @param mnoId        the ID of the new asset
     * @param mnoName          the color of the new asset
     * @param endpoint           the size for the new asset
     * @return the created asset
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Mno CreateMno(final Context ctx, final String mnoId, final String mnoName,
                           final String endpoint) {
        ChaincodeStub stub = ctx.getStub();

        if (MnoExists(ctx, mnoId)) {
            String errorMessage = String.format("Mno %s already exists", mnoId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, MnoDetailsErrors.MNO_ALREADY_EXIST.toString());
        }

        Mno mno = new Mno(mnoId, mnoName, endpoint);
        //Use Genson to convert the Asset into string, sort it alphabetically and serialize it into a json string
        String sortedJson = genson.serialize(mno);
        stub.putStringState(mnoId, sortedJson);

        return mno;
    }

    /**
     * Retrieves an mno with the specified ID from the ledger.
     *
     * @param ctx     the transaction context
     * @param mnoId the ID of the asset
     * @return the asset found on the ledger if there was one
     */
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Mno ReadMno(final Context ctx, final String mnoId) {
        ChaincodeStub stub = ctx.getStub();
        String mnoJSON = stub.getStringState(mnoId);

        if (mnoJSON == null || mnoJSON.isEmpty()) {
            String errorMessage = String.format("Mno %s does not exist", mnoId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, MnoDetailsErrors.MNO_NOT_FOUND.toString());
        }

        Mno mno = genson.deserialize(mnoJSON, Mno.class);
        return mno;
    }

    /**
     * Updates the properties of an asset on the ledger.
     *
     * @param ctx            the transaction context
     * @param mnoId        the ID of the asset being updated
     * @param mnoName          the color of the asset being updated
     * @param endpoint           the size of the asset being updated
     * @return the transferred asset
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Mno UpdateMno(final Context ctx, final String mnoId, final String mnoName,
                           final String endpoint) {
        ChaincodeStub stub = ctx.getStub();

        if (!MnoExists(ctx, mnoId)) {
            String errorMessage = String.format("Mno %s does not exist", mnoId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, MnoDetailsErrors.MNO_NOT_FOUND.toString());
        }

        Mno newMno = new Mno(mnoId, mnoName, endpoint);
        //Use Genson to convert the Asset into string, sort it alphabetically and serialize it into a json string
        String sortedJson = genson.serialize(newMno);
        stub.putStringState(mnoId, sortedJson);
        return newMno;
    }

    /**
     * Deletes asset on the ledger.
     *
     * @param ctx     the transaction context
     * @param mnoId the ID of the asset being deleted
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void DeleteMno(final Context ctx, final String mnoId) {
        ChaincodeStub stub = ctx.getStub();

        if (!MnoExists(ctx, mnoId)) {
            String errorMessage = String.format("Mno %s does not exist", mnoId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, MnoDetailsErrors.MNO_NOT_FOUND.toString());
        }

        stub.delState(mnoId);
    }

    /**
     * Checks the existence of the asset on the ledger
     *
     * @param ctx     the transaction context
     * @param mnoId the ID of the asset
     * @return boolean indicating the existence of the asset
     */
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean MnoExists(final Context ctx, final String mnoId) {
        ChaincodeStub stub = ctx.getStub();
        String mnoJson = stub.getStringState(mnoId);

        return (mnoJson != null && !mnoJson.isEmpty());
    }

    /**
     * Changes the owner of a asset on the ledger.
     *
     * @param ctx      the transaction context
     * @param assetID  the ID of the asset being transferred
     * @param newOwner the new owner
     * @return the old owner
     */
//    @Transaction(intent = Transaction.TYPE.SUBMIT)
//    public String TransferAsset(final Context ctx, final String assetID, final String newOwner) {
//        ChaincodeStub stub = ctx.getStub();
//        String assetJSON = stub.getStringState(assetID);
//
//        if (assetJSON == null || assetJSON.isEmpty()) {
//            String errorMessage = String.format("Asset %s does not exist", assetID);
//            System.out.println(errorMessage);
//            throw new ChaincodeException(errorMessage, AssetTransfer.AssetTransferErrors.ASSET_NOT_FOUND.toString());
//        }
//
//        Asset asset = genson.deserialize(assetJSON, Asset.class);
//
//        Asset newAsset = new Asset(asset.getAssetID(), asset.getColor(), asset.getSize(), newOwner, asset.getAppraisedValue());
//        //Use a Genson to conver the Asset into string, sort it alphabetically and serialize it into a json string
//        String sortedJson = genson.serialize(newAsset);
//        stub.putStringState(assetID, sortedJson);
//
//        return asset.getOwner();
//    }

    /**
     * Retrieves all assets from the ledger.
     *
     * @param ctx the transaction context
     * @return array of assets found on the ledger
     */
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllMNOs(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        List<Mno> queryResults = new ArrayList<Mno>();

        // To retrieve all assets from the ledger use getStateByRange with empty startKey & endKey.
        // Giving empty startKey & endKey is interpreted as all the keys from beginning to end.
        // As another example, if you use startKey = 'asset0', endKey = 'asset9' ,
        // then getStateByRange will retrieve asset with keys between asset0 (inclusive) and asset9 (exclusive) in lexical order.
        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");

        for (KeyValue result : results) {
            Mno mno = genson.deserialize(result.getStringValue(), Mno.class);
            System.out.println(mno);
            queryResults.add(mno);
        }

        final String response = genson.serialize(queryResults);

        return response;
    }
}
