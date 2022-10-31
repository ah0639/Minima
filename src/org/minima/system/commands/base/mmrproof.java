package org.minima.system.commands.base;

import org.minima.database.mmr.MMRData;
import org.minima.database.mmr.MMRProof;
import org.minima.objects.base.MiniData;
import org.minima.objects.base.MiniNumber;
import org.minima.objects.base.MiniString;
import org.minima.system.commands.Command;
import org.minima.utils.json.JSONObject;

public class mmrproof extends Command {

	public class mmrleafnode{
		
		public int mEntry;
		public String mData;
		public MiniData mHash;
		
		public mmrleafnode() {}
	}
	
	public mmrproof() {
		super("mmrproof","[data:] [proof:] [root:] - Check an MMR proof");
	}
	
	@Override
	public JSONObject runCommand() throws Exception{
		JSONObject ret = getJSONReply();
		
		if(!existsParam("root") || !existsParam("proof") || !existsParam("data")) {
			throw new Exception("MUST Specify both root and proof");
		}
		
		//Get the details
		String checkdata 	= getParam("data");
		MiniNumber mnum = MiniNumber.ZERO;
		String strdata 	= checkdata;
		int index = checkdata.indexOf(":");
		if(index!=-1) {
			strdata = checkdata.substring(0, index).trim();
			mnum	= new MiniNumber(checkdata.substring(index+1).trim());
		}
		
		String fullrootstr 	= getParam("root");
		MiniNumber rootnum = MiniNumber.ZERO;
		String rootstr 	= fullrootstr;
		index = fullrootstr.indexOf(":");
		if(index!=-1) {
			rootstr = fullrootstr.substring(0, index).trim();
			rootnum	= new MiniNumber(fullrootstr.substring(index+1).trim());
		}
		
		String proofstr = getParam("proof");
		
		//Is it HEX
		MiniData mdata = null;
		if(strdata.startsWith("0x")) {
			mdata = new MiniData(strdata);
		}else {
			mdata = new MiniData( new MiniString(strdata).getData() );
		}
		
		//Create the MMRdata
		MMRData mmrdata = MMRData.CreateMMRDataLeafNode(mdata, mnum);
		MMRData root 	= new MMRData( new MiniData(rootstr), rootnum);
		MiniData proof 	= new MiniData(proofstr);
		
		//Create an MMR Proof..
		MMRProof prf = MMRProof.convertMiniDataVersion(proof);
		
		//And calculate the final root value..
		MMRData prfcalc = prf.calculateProof(mmrdata);
		
		JSONObject resp = new JSONObject();
		resp.put("input", strdata);
		resp.put("data", mdata.to0xString());
		resp.put("leaf", mmrdata.toJSON());
		resp.put("finaldata", prfcalc.toJSON());
		resp.put("valid", prfcalc.isEqual(root));
		
		//Add balance..
		ret.put("response", resp);
		
		return ret;
	}

	@Override
	public Command getFunction() {
		return new mmrproof();
	}

}
