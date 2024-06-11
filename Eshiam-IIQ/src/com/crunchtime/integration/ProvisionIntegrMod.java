
package com.crunchtime.integration;

import sailpoint.object.Rule;
import java.util.HashMap;
import sailpoint.object.ProvisioningResult;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.IntegrationConfig;
import java.util.Map;
import sailpoint.api.SailPointContext;
import sailpoint.integration.AbstractIntegrationExecutor;

public class ProvisionIntegrMod extends AbstractIntegrationExecutor
{
    private SailPointContext context;
    Map configMap;
    
    public ProvisionIntegrMod() {
        this.configMap = null;
    }
    
    public void configure(final SailPointContext context, final IntegrationConfig config) throws Exception {
        super.configure(this.context = context, config);
        this.configMap = config.getAttributes().getMap();
    }
    
    public ProvisioningResult provision(final ProvisioningPlan plan) throws Exception {
        System.out.println(" Start ProvisionIntegrMod - provision()");
        ProvisioningResult provResult = new ProvisioningResult();
        if (plan != null && this.context != null) {
            System.out.println("plan : " + plan.toXml());
            System.out.println("configMap : " + this.configMap);
            final Map ruleArgs = new HashMap();
            ruleArgs.put("plan", plan);
            ruleArgs.put("configMap", this.configMap);
            final String strRuleName = (String)this.configMap.get("ruleName");
            System.out.println("strRuleName :" + strRuleName);
            final Rule ruleObject = (Rule)this.context.getObjectByName((Class)Rule.class, strRuleName);
            System.out.println("ruleObject : " + ruleObject);
            if (ruleObject == null) {
                provResult.setStatus("failed");
                provResult.addError("Role object not found with name : " + strRuleName);
            }
            else {
                provResult = (ProvisioningResult)this.context.runRule(ruleObject, ruleArgs);
            }
        }
        else {
            provResult.setStatus("failed");
            provResult.addError("The context or plan was null");
        }
        System.out.println("provResult : " + provResult);
        System.out.println(" End ProvisionIntegrMod - provision()");
        return provResult;
    }
}