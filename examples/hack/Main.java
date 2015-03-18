package hawup.examples.hack;

import java.math.BigInteger;
import java.util.Random;

import hawup.core.HaWUp;
import hawup.core.PartialResult;
import hawup.core.ResultRunnable;
import hawup.util.Publisher;


public class Main {

	public static void main(String[] args) {
		HaWUp h = new HaWUp(8,10);
		Publisher.LOGGING = true;
		final int bitsInPriv = 32;
		
		//
		// Generate a random private key
		//
		BigInteger priv     = new BigInteger(bitsInPriv,new Random());
		//
		//   and compute its public key
		//
		String     pubkey   = KeyManager.sqrAndTakeMiddle(priv);
		//
		// By trying *all* possible private keys, find a private key that corresponds
		//   to that random public key for priv
		//
		ResultRunnable rr = h.genJob(new Hack(pubkey,BigInteger.valueOf(2).pow(bitsInPriv)));
		//
		// Run on our Nodes
		//
		rr.run();
		//
		// Wait for the answer and report it
		//
		BigInteger got = new BigInteger(rr.getResult().getValue().toString());
		System.out.println("HaWUp found   private key " + got +  " whose pub is " + KeyManager.sqrAndTakeMiddle(got));
		System.out.println("My secret was private key " + priv + " whose pub is " + KeyManager.sqrAndTakeMiddle(priv));


	}

}
