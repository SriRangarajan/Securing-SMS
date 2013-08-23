package com.example.securesms;

import java.math.BigInteger;

/**
 * Class ECParameters provides parameters for an elliptic curve over GF(p). The
 * elliptic curve is y<SUP>2</SUP> = x<SUP>3</SUP> + ax + b (mod p).
 *

 */
public class ECParameters
	{

	/**
	 * Prime modulus.
	 */
	public final BigInteger p;

	/**
	 * Coefficient of x.
	 */
	public final BigInteger a;

	/**
	 * Constant coefficient.
	 */
	public final BigInteger b;

	/**
	 * Construct a new elliptic curve parameters object.
	 *
	 * @param  p  Prime modulus.
	 * @param  a  Coefficient of x.
	 * @param  b  Constant coefficient.
	 */
	public ECParameters
		(BigInteger p,
		 BigInteger a,
		 BigInteger b)
		{
		this.p = p;
		this.a = a;
		this.b = b;
		}

	}
