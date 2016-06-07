//def build(git_url, git_branch, job_name) {

def step000_provision_sut() {
    echo "Hi from new step000 method! TODO: I should be provisioning your SUT, but I'm not."
}

def step010_setup_beaker(script_dir) {
    withEnv(["SUT_HOST=${SUT_HOST}"]) {
        sh "${script_dir}/010_setup_beaker.sh"
    }
}

def step020_install_pe(SKIP_PE_INSTALL, script_dir) {
    echo "SKIP PE INSTALL?: ${SKIP_PE_INSTALL} (${SKIP_PE_INSTALL.class})"
    if (SKIP_PE_INSTALL) {
        echo "Skipping PE install because SKIP_PE_INSTALL is set."
    } else {
        sh "${script_dir}/020_install_pe.sh"
    }
}

def step030_customize_settings() {
    echo "Hi! TODO: I should be customizing PE settings on the SUT, but I'm not."
}

def step040_install_puppet_code(script_dir) {
    sh "${script_dir}/040_install_puppet_code.sh"
}

def step050_file_sync(script_dir) {
    sh "${script_dir}/050_file_sync.sh"
}

def step060_classify_nodes(script_dir) {
    withEnv(["PUPPET_GATLING_SIMULATION_CONFIG=${PUPPET_GATLING_SIMULATION_CONFIG}"]) {
        sh "${script_dir}/060_classify_nodes.sh"
    }
}

def step070_classify_nodes() {
    echo "Hi! TODO: I should be validating classification on your SUT, but I'm not."
}

def step080_launch_bg_scripts() {
    echo "Hi! TODO: I should be launching background scripts on your SUT, but I'm not."
}

def step090_run_gatling_sim(job_name, script_dir) {
    withEnv(["PUPPET_GATLING_SIMULATION_CONFIG=${PUPPET_GATLING_SIMULATION_CONFIG}",
             "PUPPET_GATLING_SIMULATION_ID=${job_name}"]) {
        sh "${script_dir}/090_run_simulation.sh"
    }
}

def step100_collect_artifacts() {
    gatlingArchive()
}

def single_pipeline(job_name) {
    def script_dir = "./jenkins-integration/jenkins-jobs/common/scripts"

    node {
        checkout scm
//        git url: git_url,
//                branch: git_branch

        SKIP_PE_INSTALL = (SKIP_PE_INSTALL == "true")

        stage '000-provision-sut'
        step000_provision_sut()

//        step([$class: 'GatlingBuildAction'])

        stage '010-setup-beaker'
        step010_setup_beaker(script_dir)

        stage '020-install-pe'
        step020_install_pe(SKIP_PE_INSTALL, script_dir)

        stage '030-customize-settings'
        step030_customize_settings()

        stage '040-install-puppet-code'
        step040_install_puppet_code(script_dir)

        stage '050-file-sync'
        step050_file_sync(script_dir)

        stage '060-classify-nodes'
        step060_classify_nodes(script_dir)

        stage '070-validate-classification'
        step070_classify_nodes()

        stage '080-launch-bg-scripts'
        step080_launch_bg_scripts()

        stage '090-run-gatling-sim'
        step090_run_gatling_sim(job_name, script_dir)

        stage '100-collect-artifacts'
        step100_collect_artifacts()
//        echo "Hi! TODO: I should be collecting the final job artifacts, but I'm not."
//        step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
//        step([$class: 'GatlingBuildAction'])

    }

}



return this;
