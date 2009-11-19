package de.mpg.mpiz.koeln.anna.server.data;

import de.mpg.mpiz.koeln.anna.data.DataBeanAccessException;

// TODO: should be <V extends DataBean>
public interface DataProxy<V> {

	/**
	 * 
	 * <p>
	 * Atomar operation on data. Data will be synchronized bevor and after this
	 * operation.
	 * </p>
	 * 
	 * @param v
	 *            Type of Data, that is accessed.
	 * @throws DataBeanAccessException
	 */
	void modifiyData(DataModifier<V> v) throws DataBeanAccessException;

	/**
	 * 
	 * <p>
	 * Use this method for reading data only. If you make changes to the data
	 * you get from this method, these changes will not be updated! If you
	 * want to write data, use {@link modifiyData()} instead.
	 * 
	 * @return the data object.
	 * @throws DataBeanAccessException
	 */
	V viewData() throws DataBeanAccessException;

}
