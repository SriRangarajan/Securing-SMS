package com.example.securesms;

import java.math.BigInteger;

/**
 * Class ECPoint provides a point on an elliptic curve over GF(p).
 *
 */
public class ECPoint
	{

	private BigInteger x;
	private BigInteger y;
	private ECParameters params;

	private static final BigInteger ZERO = BigInteger.ZERO;

	/**
	 * The point at infinity.
	 */
	public static final ECPoint OH = new ECPoint (ZERO, ZERO, null);

	/**
	 * Construct a new elliptic curve point with the given (x,y) coordinates.
	 *
	 * @param  x       X coordinate.
	 * @param  y       Y coordinate.
	 * @param  params  Elliptic curve parameters.
	 */
	public ECPoint
		(BigInteger x,
		 BigInteger y,
		 ECParameters params)
		{
		this.x = x;
		this.y = y;
		this.params = params;
		}

	/**
	 * Construct a new elliptic curve point that is a copy of the given elliptic
	 * curve point.
	 *
	 * @param  point  Elliptic curve point to copy.
	 */
	public ECPoint
		(ECPoint point)
		{
		this.x = point.x;
		this.y = point.y;
		this.params = point.params;
		}

	/**
	 * Returns this elliptic curve point's X coordinate.
	 *
	 * @return  X coordinate.
	 */
	public BigInteger x()
		{
		return x;
		}

	/**
	 * Returns this elliptic curve point's Y coordinate.
	 *
	 * @return  Y coordinate.
	 */
	public BigInteger y()
		{
		return y;
		}

	/**
	 * Returns this elliptic curve point's parameters.
	 *
	 * @return  Elliptic curve parameters.
	 */
	public ECParameters params()
		{
		return params;
		}

	/**
	 * Determine if this elliptic curve point is equal to the given object.
	 *
	 * @param  obj  Object to test.
	 *
	 * @return  True if this elliptic curve point equals obj, false otherwise.
	 */
	public boolean equals
		(Object obj)
		{
		return
			(obj instanceof ECPoint) &&
			this.x.equals (((ECPoint) obj).x) &&
			this.y.equals (((ECPoint) obj).y);
		}

	/**
	 * Negate this elliptic curve point.
	 *
	 * @return  Elliptic curve point = -this.
	 */
	public ECPoint negate()
		{
		return new ECPoint (x, y.negate().mod (params.p), params);
		}

	/**
	 * Add this elliptic curve point and the given elliptic curve point.
	 *
	 * @param  point  Elliptic curve point to add.
	 *
	 * @return  Elliptic curve point = this + point.
	 */
	public ECPoint add
		(ECPoint point)
		{
		if (this.equals (OH)) return point;
		if (point.equals (OH)) return this;
		BigInteger x0 = this.x;
		BigInteger y0 = this.y;
		BigInteger x1 = point.x;
		BigInteger y1 = point.y;
		BigInteger p = params.p;
		BigInteger lambda = null;
		if (! x0.equals (x1))
			{
			lambda =
				y0.subtract (y1) .mod (p) .multiply
				(x0.subtract (x1) .mod (p) .modInverse (p))
				.mod (p);
			}
		else if (! y0.equals (y1))
			{
			return OH;
			}
		else if (y1.equals (ZERO))
			{
			return OH;
			}
		else
			{
			BigInteger a = params.a;
			BigInteger x1sqr = x1.multiply (x1) .mod (p);
			BigInteger inv2y1 = y1.add (y1) .mod (p) .modInverse (p);
			lambda = x1sqr.add (x1sqr) .add (x1sqr) .add (a) .multiply (inv2y1)
				.mod (p);
			}
		BigInteger x2 = lambda.multiply (lambda) .mod (p) .subtract (x0)
			.subtract (x1) .mod (p);
		BigInteger y2 = x1.subtract (x2) .multiply (lambda) .mod (p)
			.subtract (y1) .mod (p);
		return new ECPoint (x2, y2, params);
		}

	/**
	 * Subtract this elliptic curve point and the given elliptic curve point.
	 *
	 * @param  point  Elliptic curve point to subtract.
	 *
	 * @return  Elliptic curve point = this - point.
	 */
	public ECPoint subtract
		(ECPoint point)
		{
		return this.add (point.negate());
		}

	/**
	 * Multiply this elliptic curve point by the given scalar.
	 *
	 * @param  n  Scalar.
	 *
	 * @param  Elliptic curve point = n*this.
	 */
	public ECPoint multiply
		(BigInteger n)
		{
		if (n.equals (ZERO)) return OH;
		ECPoint Q = null;
		BigInteger k = null;
		if (n.compareTo (ZERO) < 0)
			{
			Q = this.negate();
			k = n.negate();
			}
		else
			{
			Q = this;
			k = n;
			}
		BigInteger h = k.add (k) .add (k);
		int m = h.bitLength() - 1;
		ECPoint S = Q;
		for (int i = m - 1; i >= 1; -- i)
			{
			S = S.add (S);
			if (h.testBit (i))
				{
				if (k.testBit (i))
					{
					// h_i = 1, k_i = 1
					}
				else
					{
					// h_i = 1, k_i = 0
					S = S.add (Q);
					}
				}
			else
				{
				if (k.testBit (i))
					{
					// h_i = 0, k_i = 1
					S = S.subtract (Q);
					}
				else
					{
					// h_i = 0, k_i = 0
					}
				}
			}
		return S;
		}

	}
