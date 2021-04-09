package blockchain;

import util.StringUtil;

import java.time.Instant;

public class Block {
	static int nbBlocks = 0;
	private String previousHash;

	private String hash;

	private Transaction data;

	private int index;

	private int nonce;

	private long timestamp;

	public Block(String previousHash, Transaction data, long timestamp) {
		this.previousHash = previousHash;
		this.data = data;
		this.timestamp = timestamp;
		this.nonce = 0;
		this.index = nbBlocks;
		nbBlocks++;
		this.hash = hash();
	}

	public String toString() {
		return "=========================================== " + getIndex() + " ============================================\n"
				+ "+ Index\t\t= " + getIndex() + "\n"
				+ "+ Previous hash\t= " + this.getPreviousHash() + "\n"
				+ "+ Transaction\t= " + this.getData().toString() + "\n"
				+ "+ Timestamp\t= " + StringUtil.timestampConverter(this.getTimestamp()) + "\n"
				+ "+ Nonce\t\t= " + getNonce() + "\n"
				+ "+ Hash\t\t= " + getHash() + "\n"
				+ "==========================================================================================\n";
	}

	public String hash() {
		String input = previousHash
				+ data.toString()
				+ timestamp
				+ nonce;
		return StringUtil.SHA256(input);
	}

	public static Block genesisBlock(Transaction data){
		return newBlock(StringUtil.zeros(64),data);
	}

	public static Block newBlock(String previousHash, Transaction transaction) {
		Block block = new Block(previousHash, transaction, Instant.now().getEpochSecond());
		return block;
	}

	public String mine(int prefix){
		String prefixString = new String(new char[prefix]).replace('\0', '0');
		while (!hash.substring(0, prefix).equals(prefixString)) {
			nonce++;
			hash = hash();
		}
		return hash;
	}

	// Getters and Setters
	public static int getNbBlocks() {
		return nbBlocks;
	}

	public static void setNbBlocks(int nbBlocks) {
		Block.nbBlocks = nbBlocks;
	}

	public String getPreviousHash() {
		return previousHash;
	}

	public void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public Transaction getData() {
		return data;
	}

	public void setData(Transaction data) {
		this.data = data;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getNonce() {
		return nonce;
	}

	public void setNonce(int nonce) {
		this.nonce = nonce;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
