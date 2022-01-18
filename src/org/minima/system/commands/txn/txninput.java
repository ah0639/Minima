package org.minima.system.commands.txn;

import org.minima.objects.Coin;
import org.minima.objects.Transaction;
import org.minima.objects.base.MiniData;
import org.minima.system.brains.TxPoWSearcher;
import org.minima.system.commands.Command;
import org.minima.system.commands.txn.txndb.TxnDB;
import org.minima.utils.json.JSONObject;

public class txninput extends Command {

	public txninput() {
		super("txninput","[id:] [coinid:] - Add a coin as an input to a transaction");
	}
	
	@Override
	public JSONObject runCommand() throws Exception {
		JSONObject ret = getJSONReply();

		TxnDB db = TxnDB.getDB();
		
		//The transaction
		String id = getParam("id");
		
		//The Coin
		String coinid = getParam("coinid");
		
		//Get the coin
		Coin cc = TxPoWSearcher.searchCoins(new MiniData(coinid));
		
		//Get the Transaction
		Transaction trans = db.getTransactionRow(id).getTransaction();
		trans.addInput(cc);
		
		//Output the current trans..
		ret.put("response", db.getTransactionRow(id).toJSON());
		
		return ret;
	}

	@Override
	public Command getFunction() {
		return new txninput();
	}

}
