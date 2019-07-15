package com.overlap.train;

import com.overlap.entity.TechnicalFeature;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.DecisionTreeClassificationModel;
import org.apache.spark.ml.classification.DecisionTreeClassifier;
import org.apache.spark.ml.classification.RandomForestClassificationModel;
import org.apache.spark.ml.classification.RandomForestClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.*;

public class Train {

    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("TrainOverlapDecisionTree")
                .master("local[*]")
                .getOrCreate();

        Encoder<TechnicalFeature> encoder = Encoders.bean(TechnicalFeature.class);

        Dataset<TechnicalFeature> data = spark.read()
                .option("multiline", true)
                .json("src/main/resources/overlap/train.json").as(encoder);

        data.show(10);

//        String impurity = "gini";
        String impurity = "entropy";
        int maxDepth = 7;
        int maxBins = 32;

        Dataset<TechnicalFeature>[] splits = data.randomSplit(new double[]{0.6, 0.4});
        Dataset<TechnicalFeature> trainingData = splits[0];
        Dataset<TechnicalFeature> testData = splits[1];

        VectorAssembler assembler = new VectorAssembler()
                .setInputCols(new String[] {
                        // 0
                        "firstNameDistance",
                        // 1
                        "lastNameDistance",
                        // 2
                        "middleNameDistance",
                        // 3
                        "dulEquals",
                        // 4
                        "brDateMistakes",
                        // 5
                        "phoneDistance"})
                .setOutputCol("features");

//        DecisionTreeClassifier dt = new DecisionTreeClassifier()
//                .setLabelCol("result")
//                .setFeaturesCol("features")
//                .setImpurity(impurity)
//                .setMaxDepth(maxDepth)
//                .setMaxBins(maxBins);

        RandomForestClassifier dt = new RandomForestClassifier()
                .setLabelCol("result")
                .setFeaturesCol("features")
                .setImpurity(impurity)
                .setMaxDepth(maxDepth)
                .setMaxBins(maxBins)
                .setNumTrees(4);

        Pipeline pipeline = new Pipeline()
                .setStages(new PipelineStage[]{assembler, dt});

        PipelineModel model = pipeline.fit(trainingData);

        // Make predictions.
        Dataset<Row> predictions = model.transform(testData);

        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setLabelCol("result")
                .setPredictionCol("prediction")
                .setMetricName("accuracy");
        double accuracy = evaluator.evaluate(predictions);
        System.out.println("!!!!!\nTest Error = " + (1.0 - accuracy));

        predictions.where("result != prediction").show(100);
        System.out.println("!!!!!\n" + predictions.where("result != prediction").count());

//        DecisionTreeClassificationModel treeModel =
//                (DecisionTreeClassificationModel) (model.stages()[1]);
        RandomForestClassificationModel treeModel =
                (RandomForestClassificationModel) (model.stages()[1]);
        System.out.println("Learned classification tree model:\n" + treeModel.toDebugString());

    }
}
