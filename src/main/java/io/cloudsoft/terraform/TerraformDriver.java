package io.cloudsoft.terraform;

import java.util.Map;

import org.apache.brooklyn.entity.software.base.SoftwareProcessDriver;

import static java.lang.String.format;

public interface TerraformDriver extends SoftwareProcessDriver {

    String PLAN_STATUS = "tf.plan.status";
    String PLAN_PROVIDER = "tf.plan.provider";
    String RESOURCE_CHANGES = "tf.resource.changes";
    String PLAN_MESSAGE = "tf.plan.message";

    void customize();
    void launch();
    void postLaunch();
    int destroy();

    Map<String, Object> runJsonPlanTask();
    String runPlanTask();
    void runApplyTask();
    String runOutputTask();
    String runShowTask();
    int runDestroyTask();

    // added these methods to underline the terraform possible commands
    default String initCommand() {
        return makeTerraformCommand("init -input=false"); // Prepare your working directory for other commands
    }
    default String jsonPlanCommand() {
        return makeTerraformCommand("plan -out=tfplan -lock=false -input=false -no-color -json"); // Show changes required by the current configuration
    }
    default String planCommand() {
        return makeTerraformCommand("plan -lock=false -input=false -no-color"); // Show changes required by the current in the normal TF style, provides more info than the json version
    }
    default String applyCommand() {
        return makeTerraformCommand("apply -no-color -input=false tfplan"); // Create or update infrastructure
    }
    default String lightApplyCommand() {
        return makeTerraformCommand(" apply -refresh-only -auto-approve -no-color -input=false tfplan"); // Create or update infrastructurecd
    }
    default String showCommand() {
        return makeTerraformCommand("show -no-color -json"); // Show the current state or a saved plan
    }
    default String outputCommand() {
        return makeTerraformCommand("output -no-color -json"); // Show output values from your root module
    }
    default String destroyCommand() {
        return makeTerraformCommand("apply -destroy -auto-approve -no-color");
    }

    default String makeTerraformCommand(String argument) {
        return format("cd %s && %s/terraform %s", getRunDir(), getInstallDir(), argument);
    }

    default String getConfigurationFilePath() {
        return getRunDir() + "/configuration.tf";
    }

    default String getTfVarsFilePath() {
        return getRunDir() + "/terraform.tfvars";
    }

    default String getStateFilePath() {
        return getRunDir() + "/terraform.tfstate";
    }

    // these are just here to allow the terraform commands building methods to be default too :)
    String getRunDir();
    String getInstallDir();

}
