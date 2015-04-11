import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class BayesNet {
	ArrayList<Node> network;
	Random rnd = new Random();
	
	BayesNet(ArrayList<Node> network){
		this.network = network;
	}
	
	Node getRandomNotInEvidence(ArrayList<Node> evidence){
		
		int randomIndex = rnd.nextInt(network.size());
		Node retNode = network.get(randomIndex);
		while(evidence.indexOf(retNode) != -1){
			randomIndex = rnd.nextInt(network.size());
			retNode = network.get(randomIndex);
		}
		
		return retNode;
	}
	
	boolean sampleCorrectGivenEvidence(ArrayList<Node> evidence, int[] sample){
		for(Node node : evidence){
			int pos = node.nodePos - 1;
			int evidenceState = (node.getState()) ? 1 : 0;
			if(sample[pos] != evidenceState){
				return false;
			}
		}
		return true;
	}
	
	double queryNetwork(Node sample, ArrayList<Node> evidence, int itrs, int samples){
		
		Node randomNode = getRandomNotInEvidence(evidence);
		boolean startState = sample.getState();
		// Randomly initialize the network without changing evidence
		for(Node node: network){
			if(evidence.indexOf(node) == -1){
				node.setState(rnd.nextBoolean());
			}else{
				System.out.println("It is in evidence");
			}
		}
		int[][] sampleList = new int[samples][network.size()];
		for(int i = 0; i < samples; i++)
		{
			for(int j = 0; j < itrs; j++){
				
				double prob = randomNode.getConditionalProb();
				if(rnd.nextDouble() < prob)
				{
					randomNode.setState(true);
				}else{
					randomNode.setState(false);
				}
				
				randomNode = getRandomNotInEvidence(evidence);
			}
			for(int k = 0; k < network.size(); k++){
				sampleList[i][k] = (network.get(k).getState()) ? 1 : 0 ;
			}
		}
		
		double numer = 0;
		double denom = 0;
		sample.setState(startState);
		for(int i = 0; i < samples; i++){
			if(sampleCorrectGivenEvidence(evidence, sampleList[i])){
				denom++;
				if(sampleCorrectGivenEvidence(new ArrayList<Node>(Arrays.asList(sample)), sampleList[i])){
					numer++;
				}
			}
			for(int j = 0; j < network.size(); j++)
			{
				System.out.print(" " + sampleList[i][j] + " ");
			}
			System.out.println("");
		}
		
		
		
		
		return numer/ denom;
	}
	
}