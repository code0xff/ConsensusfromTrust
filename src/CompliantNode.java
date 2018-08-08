import java.util.HashSet;
import java.util.Set;

/* CompliantNode refers to a node that follows the rules (not malicious)*/
public class CompliantNode implements Node {
	private double p_graph;
	private double p_malicious;
	private double p_txDistribution;
	private int numRounds;

	private boolean[] followees;

	private Set<Transaction> pendingTransactions;
	private boolean[] maliciousFollowees;

	public CompliantNode(double p_graph, double p_malicious, double p_txDistribution, int numRounds) {
		this.p_graph = p_graph;
		this.p_malicious = p_malicious;
		this.p_txDistribution = p_txDistribution;
		this.numRounds = numRounds;
	}

	public void setFollowees(boolean[] followees) {
		this.followees = followees;
		this.maliciousFollowees = new boolean[followees.length];
	}

	public void setPendingTransaction(Set<Transaction> pendingTransactions) {
		this.pendingTransactions = pendingTransactions;
	}

	public Set<Transaction> sendToFollowers() {
		Set<Transaction> sendTx = new HashSet<>(pendingTransactions);
		pendingTransactions.clear();

		return sendTx;
	}

	public void receiveFromFollowees(Set<Candidate> candidates) {
		Set<Integer> senders = new HashSet<>();
		for (Candidate candidate : candidates) {
			senders.add(candidate.sender);
		}
		for (int i = 0; i < followees.length; i++) {
			if (followees[i] && !senders.contains(i))
				maliciousFollowees[i] = true;
		}
		for (Candidate c : candidates) {
			if (!maliciousFollowees[c.sender]) {
				pendingTransactions.add(c.tx);
			}
		}
	}
}
