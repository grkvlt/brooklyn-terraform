package io.cloudsoft.terraform;

import java.io.IOException;
import java.util.Map;

import org.apache.brooklyn.api.location.OsDetails;
import org.apache.brooklyn.entity.software.base.SoftwareProcessDriver;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import static java.lang.String.format;

public interface TerraformDriver extends SoftwareProcessDriver {

    Map<String, Object> getState() throws JsonParseException, JsonMappingException, IOException;

    int destroy();

    boolean runPlanTask();
    void runApplyTask();
    int runDestroyTask();

    // added these methods to underline the terraform possible commands
    default String initCommand() {
        return makeTerraformCommand("init -input=false"); // Prepare your working directory for other commands
    }
    default String planCommand() {
        //TODO consider adding -json here as well after the model is ready and just look for the changes -> change number.
        return makeTerraformCommand("plan -out=tfplan -lock=false -no-color"); // Show changes required by the current configuration
    }
    default String applyCommand() {
        return makeTerraformCommand("apply -no-color -input=false tfplan"); // Create or update infrastructure
    }
    default String showCommand() {
        return makeTerraformCommand("show -no-color -json"); // Show the current state or a saved plan
    }
    default String refreshCommand() {
        return makeTerraformCommand("refresh -input=false -no-color -json"); // update the state to match remote systems
    }
    default String outputCommand() {
        return makeTerraformCommand("output -no-color -json"); // Show output values from your root module
    }
    default String destroyCommand() {
        return makeTerraformCommand("destroy -auto-approve -no-color");
    }

    default String makeTerraformCommand(String argument) {
        return format("cd %s && %s/terraform %s", getRunDir(), getInstallDir(), argument);
    }

    // these are just here to allow the terraform commands building methods to be default too :)
    String getRunDir();
    String getInstallDir();


    default String getConfigurationFilePath() {
        return getRunDir() + "/configuration.tf";
    }

    default String getStateFilePath() {
        return getRunDir() + "/terraform.tfstate";
    }
}
