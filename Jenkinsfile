pipeline {

    agent any

    options {
        timeout(time: 30, unit: 'MINUTES')
        disableConcurrentBuilds()
    }

    stages {
        stage('gradle clean build') {
            agent {
                docker {
                    image 'gradle:4.7-jdk8-alpine'
                    reuseNode true
                }
            }

            steps {
                script {
                    sh 'gradle clean'
                    sh 'gradle :demo-api:build'
                    sh 'gradle :demo-client:build'
                }
            }
        }

        stage('publish') {
            steps {
                script {
                    def jarServer = Artifactory.newServer url: 'http://localhost:8081/artifactory/'
                    jarServer.username = 'admin'
                    jarServer.password = 'password'
                    def rtGradle = Artifactory.newGradleBuild()

                    rtGradle.usesPlugin = true
                    rtGradle.tool = 'gradle'
                    rtGradle.resolver server: jarServer, repo: 'libs-snapshot-local'
                    rtGradle.deployer server: jarServer, repo: 'libs-snapshot-local'
                    rtGradle.deployer.deployMavenDescriptors = true
                    rtGradle.deployer.deployIvyDescriptors = false

                    rtGradle.run rootDir: "./", buildFile: 'build.gradle', tasks: 'clean artifactoryPublish'
                    def buildInfo1 = rtGradle.run rootDir: "./", buildFile: 'build.gradle', tasks: ':demo-api:artifactoryPublish :demo-client:artifactoryPublish'
                    def buildInfo2 = rtGradle.run rootDir: "./", buildFile: 'build.gradle', tasks: ':demo-impl:artifactoryPublish'
                    jarServer.publishBuildInfo buildInfo1
                    jarServer.publishBuildInfo buildInfo2
                }
            }
        }

    }

}
