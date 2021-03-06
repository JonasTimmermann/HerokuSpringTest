package de.emp2020.alertManager;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PromResponse {

	String status;			// HTTP status of the request
	Data data;				// content of the response
	
	
	/**
	 * Will check if response returned any valid data
	 * @return
	 * 		{@code true} if response contained valid data
	 */
	public boolean isTriggered ()
	{
		return !(data == null || data.isTriggered());
	}
	
	
	// AUTOGENERATED METHODS
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	
	public String toString ()
	{
		return "{Status:" + status + ", Data:" + data + "}";
	}

	
	// internal Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public class Data {
		
		String resultType;			// Prometheus result type, is expected to be a vector, but currently doesn't differentiate
		@JsonProperty(value="result")
		List<Result> result;		// a list of all results, currently ignored
		
		
		/**
		 * Will check if the data contains any valid results
		 * @return
		 * 		{@code false} if results are valid
		 */
		public boolean isTriggered ()
		{
			return result == null || result.isEmpty();
		}
		
		
		// AUTOGENERATED METHODS AND CLASSES

		public String getResultType() {
			return resultType;
		}

		public void setResultType(String resultType) {
			this.resultType = resultType;
		}
		
		
		public List<Result> getResult() {
			return result;
		}

		public void setResult(List<Result> result) {
			this.result = result;
		}

		public String toString ()
		{
			String str = "{Result Type:" + resultType + ", Result:[";
			for (Result temp : result)
			{
				str += temp.toString() + ", ";
			}
			return str.substring(0, str.length()) + "]}";
		}
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Result {
		
		Metric metric;
		List<Double> value;
		
		public Metric getMetric() {
			return metric;
		}

		public void setMetric(Metric metric) {
			this.metric = metric;
		}

		public List<Double> getValue() {
			return value;
		}

		public void setValue(List<Double> value) {
			this.value = value;
		}
		
		public String toString ()
		{
			String str = "{Result Type:" + metric + ", Result:[";
			for (Double temp : value)
			{
				str += temp.toString() + ", ";
			}
			return str.substring(0, str.length()) + "]}";
		}
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Metric {
		
		@JsonProperty(value="__name__")
		String name;
		
		String instance;
		
		String job;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getInstance() {
			return instance;
		}

		public void setInstance(String instance) {
			this.instance = instance;
		}

		public String getJob() {
			return job;
		}

		public void setJob(String job) {
			this.job = job;
		}
		
		public String toString ()
		{
			return "{Name:" + name + ", Instance:" + instance + ", Job:" + job + "}";
		}
		
	}

}
