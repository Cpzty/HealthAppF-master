package com.example.cristian.healthapp;

import com.google.gson.JsonObject;
import com.ibm.watson.developer_cloud.http.RequestBuilder;
import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.ListModelsResults;
import com.ibm.watson.developer_cloud.service.WatsonService;
import com.ibm.watson.developer_cloud.util.GsonSingleton;
import com.ibm.watson.developer_cloud.util.ResponseConverterUtils;
import com.ibm.watson.developer_cloud.util.Validator;

public class NaturalLanguageUnderstanding extends WatsonService {

    private static final String SERVICE_NAME = "natural_language_understanding";
    private static final String URL = "https://gateway.watsonplatform.net/natural-language-understanding/api";

    private String versionDate;

    /** The Constant VERSION_DATE_2017_02_27. */
    public static final String VERSION_DATE_2017_02_27 = "2017-02-27";

    /**
     * Instantiates a new `NaturalLanguageUnderstanding`.
     *
     * @param versionDate The version date (yyyy-MM-dd) of the REST API to use. Specifying this value will keep your API
     *        calls from failing when the service introduces breaking changes.
     */
    public NaturalLanguageUnderstanding(String versionDate) {
        super(SERVICE_NAME);
        if ((getEndPoint() == null) || getEndPoint().isEmpty()) {
            setEndPoint(URL);
        }

        Validator.isTrue((versionDate != null) && !versionDate.isEmpty(),
                "'version cannot be null. Use " + VERSION_DATE_2017_02_27);

        this.versionDate = versionDate;
    }

    /**
     * Instantiates a new `NaturalLanguageUnderstanding` with username and password.
     *
     * @param versionDate The version date (yyyy-MM-dd) of the REST API to use. Specifying this value will keep your API
     *        calls from failing when the service introduces breaking changes.
     * @param username the username
     * @param password the password
     */
    public NaturalLanguageUnderstanding(String versionDate, String username, String password) {
        this(versionDate);
        setUsernameAndPassword(username, password);
    }

    /**
     * Analyzes text, HTML, or a public webpage with one or more text analysis features.
     *
     * @param parameters An object containing request parameters. The `features` object and one of the `text`, `html`, or
     *        `url` attributes are required.
     * @return the {@link AnalysisResults} with the response
     */
    public ServiceCall<AnalysisResults> analyze(AnalyzeOptions parameters) {
        RequestBuilder builder = RequestBuilder.post("/v1/analyze");
        builder.query(VERSION, versionDate);

        if (parameters != null) {
            builder.bodyJson(GsonSingleton.getGson().toJsonTree(parameters).getAsJsonObject());
        } else {
            builder.bodyJson(new JsonObject());
        }

        return createServiceCall(builder.build(), ResponseConverterUtils.getObject(AnalysisResults.class));
    }

    /**
     * Delete model.
     *
     * Deletes a custom model.
     *
     * @param modelId the model id
     * @return the service call
     */
    public ServiceCall<Void> deleteModel(String modelId) {
        Validator.notNull(modelId, "modelID cannot be null");
        RequestBuilder builder = RequestBuilder.delete(String.format("/v1/models/%s", modelId));
        builder.query(VERSION, versionDate);
        return createServiceCall(builder.build(), ResponseConverterUtils.getVoid());
    }

    /**
     * List models.
     *
     * Lists available models for Relations and Entities features, including Watson Knowledge Studio custom models that
     * you have created and linked to your Natural Language Understanding service.
     *
     * @return the {@link ListModelsResults} with the response
     */
    public ServiceCall<ListModelsResults> getModels() {
        RequestBuilder builder = RequestBuilder.get("/v1/models");
        builder.query(VERSION, versionDate);
        return createServiceCall(builder.build(), ResponseConverterUtils.getObject(ListModelsResults.class));
    }

}
