package blockchain;

import util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {
	private static Blockchain instance = null;

	int prefix;
	public List<Block> blockchain;
	String prefixString;

	// Singleton
	private Blockchain(int prefix){
		this.prefix = prefix;
		this.blockchain = new ArrayList<>();
		this.prefixString = new String(new char[prefix]).replace('\0', '0');
	}

	public static Blockchain getInstance(int prefix){
		if (instance == null) {
			instance = new Blockchain(prefix);
		}
		return instance;
	}

	public boolean verify() {
		boolean flag = true;
		for (int i = 0; i < blockchain.size(); i++){
			String previousHash = i==0 ? StringUtil.zeros(64) : blockchain.get(i-1).getHash();
			flag = blockchain.get(i).getHash().equals(blockchain.get(i).hash())
					&& previousHash.equals(blockchain.get(i).getPreviousHash())
					&& blockchain.get(i).getHash().substring(0, prefix).equals(prefixString);
			if (!flag) break;
		}
		return flag;
	}

	public Block addBlock(Transaction data) {
		Block block;
		if (this.isEmpty()) {
			block = Block.genesisBlock(data);
		} else {
			block = Block.newBlock(getLastHash(), data);
		}
		block.setHash(block.mine(this.prefix));
		blockchain.add(block);
		return block;
	}

	public Block getLastBlock() {
		return this.blockchain.get(this.blockchain.size() - 1);
	}

	public String getLastHash() {
		return this.getLastBlock().getHash();
	}

	public boolean isEmpty() {
		return blockchain.size() == 0;
	}

	public List<Block> getBlockchain() {
		return blockchain;
	}
}
