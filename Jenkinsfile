def appName = "oauth-client"

def gitBranch = env.BRANCH_NAME
def gitUrl = scm.repositories[0].uris[0].toString()
def pipelineBranch = 'master'
def pipelineUrl = 'ssh://git@stash.hybris.com:7999/caas2/caas-pipeline.git'

node {
    println "Loading pipeline script from uri='${pipelineUrl}', branch='${pipelineBranch}', script='pipelines/caas-app.groovy'"
    pipeline    = loadScript url: pipelineUrl, filename: "pipelines/caas-app.groovy",  branch: pipelineBranch
    caasUtils   = loadScript url: pipelineUrl, filename: "pipelines/utils.groovy",  branch: pipelineBranch

}

pipeline.execute appName: appName,isAutomerge: true, gitUrl: gitUrl, pipelineBranch: pipelineBranch, caasUtils: caasUtils
