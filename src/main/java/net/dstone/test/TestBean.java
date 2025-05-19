package net.dstone.test;

public class TestBean {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//TestBean.installForAi();
		
		TestBean.testAi();
	}

	
	private static void test() {

		String DBID = "DBID_3";
		net.dstone.common.utils.DbUtil db = null;
		net.dstone.common.utils.DataSet ds = new net.dstone.common.utils.DataSet();
		
		StringBuffer sql = new StringBuffer();
		String TABLE_NAME = "TB_RP_REL_PRSN_M";
		
		net.dstone.common.utils.DateUtil.stopWatchStart("연습장");
		try {
			sql.append("SELECT ").append("\n"); 
			sql.append("	* ").append("\n");
			sql.append("FROM  ").append("\n");
			sql.append("	" + TABLE_NAME + "  ").append("\n");
			sql.append("WHERE 1=1 ").append("\n");
			sql.append("AND ROWNUM < 10 ").append("\n");
			
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();
			db.setQuery(sql.toString());
			ds.buildFromResultSet(db.select(), "");	
			ds.checkData();
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(db.getQuery());
		}finally{
			if(db != null){
				db.release();
			}
			net.dstone.common.utils.DateUtil.stopWatchEnd("연습장");
		}
	}
	

	private static void installForAi() {
		net.dstone.common.utils.DateUtil.stopWatchStart("installForAi");
		try {

			org.bytedeco.javacpp.Loader.load(org.nd4j.nativeblas.Nd4jCpu.class);
			
		    
		} catch (UnsatisfiedLinkError e) {
			e.printStackTrace();
			
			try {
			    String path = org.bytedeco.javacpp.Loader.cacheResource(org.nd4j.nativeblas.Nd4jCpu.class, "windows-x86_64/jniNd4jCpu.dll").getPath();
			    new ProcessBuilder("C:/Temp/AI/Dependencies_x64_Release/DependenciesGui.exe", path).start().waitFor();
			} catch (Exception e2) {
				e2.printStackTrace();
			}


		    
		} catch (Exception  e) {  
			e.printStackTrace();
		}finally{
			net.dstone.common.utils.DateUtil.stopWatchEnd("installForAi");
		}
	}
	
	private static void testAi() {
		net.dstone.common.utils.DateUtil.stopWatchStart("testAi");

		try {
			
			
			//System.setProperty("org.deeplearning4j.datasets.basepath", "C:/Temp/AI");
			
		    // MNIST 데이터셋을 불러옵니다.
		    org.nd4j.linalg.dataset.api.iterator.DataSetIterator mnistTrain = new org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator(64, true, 12345);
		    org.nd4j.linalg.dataset.api.iterator.DataSetIterator mnistTest = new org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator(64, false, 12345);
		
		    // 신경망 구성 설정
		    org.deeplearning4j.nn.conf.MultiLayerConfiguration conf = new org.deeplearning4j.nn.conf.NeuralNetConfiguration.Builder()
		        .seed(12345)
		        .optimizationAlgo(org.deeplearning4j.nn.api.OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
		        .list()
		        .layer(0, new org.deeplearning4j.nn.conf.layers.DenseLayer.Builder()
		            .nIn(28 * 28) // 입력 크기
		            .nOut(100) // 첫 번째 은닉층의 뉴런 수
		            .activation(org.nd4j.linalg.activations.Activation.RELU)
		            .build())
		        .layer(1, new org.deeplearning4j.nn.conf.layers.OutputLayer.Builder(org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
		            .nIn(100) // 첫 번째 은닉층의 출력 크기
		            .nOut(10) // 출력층의 뉴런 수 (10개의 클래스)
		            .activation(org.nd4j.linalg.activations.Activation.SOFTMAX)
		            .build())
		        .build();
		
		    // 다층 퍼셉트론(MLP) 생성
		    org.deeplearning4j.nn.multilayer.MultiLayerNetwork model = new org.deeplearning4j.nn.multilayer.MultiLayerNetwork(conf);
		    model.init();
		    model.setListeners(new org.deeplearning4j.optimize.listeners.ScoreIterationListener(10));
		
		    // 모델 훈련
		    for (int i = 0; i < 5; i++) { // 5 에포크 동안 훈련
		        model.fit(mnistTrain);
		        System.out.println("Epoch " + i + " complete");
		    }
		
		    // 모델 평가
		    org.deeplearning4j.eval.Evaluation eval = model.evaluate(mnistTest);
		    System.out.println(eval.stats());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			net.dstone.common.utils.DateUtil.stopWatchEnd("testAi");
		}
	}
}
