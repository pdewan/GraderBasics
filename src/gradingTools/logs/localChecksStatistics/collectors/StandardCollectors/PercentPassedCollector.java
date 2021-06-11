package gradingTools.logs.localChecksStatistics.collectors.StandardCollectors;

import gradingTools.logs.localChecksStatistics.collectors.AbstractCollector;
import gradingTools.logs.localChecksStatistics.collectors.Collector;

public class PercentPassedCollector extends AbstractCollector{
	
	final double [] cutOffs;
	
	public PercentPassedCollector(int fraction){
		double amount = 100.0/fraction;
		double [] cutOffs = new double [fraction];
		String [] headers = new String [fraction];
		for(int i=0;i<fraction;i++){
			cutOffs[i]=Math.round(100*(i+1)*amount)/100.0;
			headers[i]="Num until "+cutOffs[i]+"%";
		}
		this.reqPass=1;
		this.cutOffs=cutOffs;
		this.headers=headers;
		reset();
	}

	@Override
	public void logData(String[] data) throws IllegalArgumentException{
		try{
			double percentPassed = Double.parseDouble(data[PERCENT_PASSED_INDEX]);
			for(int i=0;i<cutOffs.length;i++)
				if(percentPassed>=cutOffs[i]){
					if(results[i]==null)
						results[i]=Integer.toString(Integer.parseInt(data[TEST_NUMBER_INDEX])+1);
				}else
					return;
		}catch(Exception e){
			throw new IllegalArgumentException("Expected Parseable Double in Column:" + Collector.PERCENT_PASSED_INDEX);
		}
		
	}

	@Override
	public boolean requiresTestNames() {
		return false;
	}

	@Override
	protected String getHeaderPhrase() {
		return null;
	}


}
