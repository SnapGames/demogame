package core.math;

import java.util.Collection;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A 2D Vector class to compute next gen things..
 *
 * @author Frédéric Delorme<frederic.delorme@snapgames.fr>
 */
@Data
@NoArgsConstructor
public class Vector2D {

	private String name;

	/**
	 * X axe factor.
	 */
	public float x = 0.0f;
	/**
	 * Y axe factor.
	 */
	public float y = 0.0f;

	public final static Vector2D ZERO = new Vector2D(0.0f, 0.0f);
	public final static Vector2D UNITY = new Vector2D(1.0f, 1.0f);

	/**
	 * Create a Vector2D
	 */
	public Vector2D(String name) {
		this.x = 0.0f;
		this.y = 0.0f;
		name = "v_noname";
	}

	/**
	 * Set the default gravity.
	 *
	 * @param x
	 * @param y
	 */
	public Vector2D(String name, float x, float y) {
		this.name = name;
		this.x = x;
		this.y = y;
	}

	/**
	 * Set the default gravity.
	 *
	 * @param x
	 * @param y
	 */
	public Vector2D(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * add the v vector.
	 *
	 * @param v
	 */
	public Vector2D add(Vector2D v) {
		this.x += v.x;
		this.y += v.y;
		return this;
	}

	/**
	 * Add a list of vector to the current vector.
	 * 
	 * @param forces a List of Vector2D to be added.
	 */
	public Vector2D addAll(Collection<Vector2D> forces) {
		for (Vector2D f : forces) {
			add(f);
		}
		return this;
	}

	/**
	 * substract the v vector.
	 *
	 * @param v
	 */
	public Vector2D sub(Vector2D v) {
		return new Vector2D(this.name, x - v.x, y - v.y);
	}

	/**
	 * multiply the vector with f.
	 *
	 * @param f
	 */
	public Vector2D multiply(float f) {
		this.x *= f;
		this.y *= f;
		return this;
	}

	/**
	 * Compute distance between this vector and the vector <code>v</code>.
	 *
	 * @param v the vector to compute distance with.
	 * @return
	 */
	public float distance(Vector2D v) {
		float v0 = x - v.x;
		float v1 = y - v.y;
		return (float) Math.sqrt(v0 * v0 + v1 * v1);
	}

	/**
	 * Normalization of this vector.
	 */
	public Vector2D normalize() {
		// sets length to 1
		//
		double length = Math.sqrt(x * x + y * y);

		if (length != 0.0) {
			float s = 1.0f / (float) length;
			x = x * s;
			y = y * s;
		}

		return new Vector2D(this.name, x, y);
	}

	/**
	 * Dot product for current instance {@link Vector2D} and the <code>v1</code>
	 * vector.
	 *
	 * @param v1
	 * @return
	 */
	public double dot(Vector2D v1) {
		return this.x * v1.x + this.y * v1.y;
	}

	public String toString() {
		return String.format("(%03.4f,%03.4f)", x, y);
	}

	public Vector2D maximize(float max) {
		x = (Math.abs(x) > Math.abs(max) ? Math.signum(x) * max : x);
		y = (Math.abs(y) > Math.abs(max) ? Math.signum(y) * max : y);
		return this;
	}

	public Vector2D minimize(float min) {
		x = (Math.abs(x) < Math.abs(min) ? Math.signum(x) * min : x);
		y = (Math.abs(y) < Math.abs(min) ? Math.signum(y) * min : y);
		return this;
	}

	public Vector2D threshold(float thresholdValue) {
		x = (Math.abs(x) < Math.abs(thresholdValue) ? 0.0f : x);
		y = (Math.abs(y) < Math.abs(thresholdValue) ? 0.0f : y);

		return this;

	}

	public String getName() {
		return name;
	}

	public boolean isZero() {
		return (x == 0.0f && y == 0.0f);
	}

	public boolean isUnity() {
		return (x == 1.0f && y == 1.0f);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector2D other = (Vector2D) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}

	public boolean greaterThan(Vector2D nextPosition) {
		return x > nextPosition.x && y > nextPosition.y;
	}

}