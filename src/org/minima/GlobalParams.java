package org.minima;

import org.minima.objects.base.MiniNumber;

public class GlobalParams {

	/**
	 * Which Version
	 */
	public static String MINIMA_VERSION = "0.98.35"; 
	
	/**
	 * Number of seconds before sending a pulse message - every 10 minutes
	 */
	public static int USER_PULSE_FREQ   = 10 * 60;
	
	/**
	 * Speed in blocks per second.. 
	 * -  0.05  = 20 second block time
	 */
	public static MiniNumber MINIMA_BLOCK_SPEED  = new MiniNumber("0.05");
	
	/**
	 * When checking speed and average difficulty only look at this many blocks back
	 */
	public static MiniNumber MINIMA_BLOCKS_SPEED_CALC = new MiniNumber(1024);
	
	/**
	 * How deep before we think confirmed..
	 */
	public static MiniNumber MINIMA_CONFIRM_DEPTH  = new MiniNumber("3");
	
	/**
	 * How often do we cascade the chain
	 */
	public static MiniNumber MINIMA_CASCADE_FREQUENCY = new MiniNumber(100);
	
	/**
	 * Depth before we cascade..
	 */
	public static MiniNumber MINIMA_CASCADE_START_DEPTH = new MiniNumber(4320);
	
	/**
	 * Number of blocks at each cascade level 
	 */
	public static int MINIMA_CASCADE_LEVEL_NODES  = 128;
	
	/**
	 * How Many Cascade Levels
	 */
	public static int MINIMA_CASCADE_LEVELS  = 32;
	
	/**
	 * Current default HASH_Strength Used. Can be up to 512.
	 * All the MINING, TxPoW and MMR data ALWAYS uses 512. But addresses, scripts, and public keys..
	 * can be set to less. This way signatures and addresses are shorter.
	 */
	public static int MINIMA_DEFAULT_HASH_STRENGTH = 160;
	
	/**
	 * Max Proof History - how far back to use a proof of coin..
	 * If there is a re-org of more than this the proof will be invalid 
	 */
	public static MiniNumber MINIMA_MMR_PROOF_HISTORY = new MiniNumber(256);
	
	/**
	 * Just create a block every transaction. Useful when not mining 
	 * and just want a block every single transaction to debug.
	 * Automatically disables the auto mining
	 */
	public static boolean MINIMA_ZERO_DIFF_BLK  = false;

}
