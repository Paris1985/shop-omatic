pipeline {

    agent any
    tools {
        maven 'M3'
    }
    stages {
        stage ('Checkout') {
            steps {
                checkout scmGit(branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Paris1985/shop-omatic']])
            }
        }

        stage ('Build') {
            steps {
                sh 'mvn clean install'
            }
            post {
                always {
                  publishCoverage adapters: [jacocoAdapter('target/site/jacoco/jacoco.xml')]
                }
            }
        }

        stage ('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar -Dsonar.projectKey=shop-omatic -Dsonar.projectName=shop-omatic -Dsonar.host.url=http://localhost:9000 -Dsonar.token=sqp_c145b9d8e9f9a3a6d46775d27e314c0175454bef'
                }
            }
        }

        stage("Quality Gate") {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage("Upload to JFrog") {
            steps {
              sh 'mvn deploy'
            }
        }

        stage ('DEV Approve') {
          steps {

          echo "Deployed to DEV Approval"
          slackSend channel: 'exciting-shop-omatic-experience', message: 'Dev deployment is needing your approval - http://localhost:9999/job/shop-omatic/', teamDomain: 'shop-omatic', tokenCredentialId: 'slacksecret'


          echo "Taking approval from DEV Manager for QA Deployment"
            timeout(time: 7, unit: 'DAYS') {
            input message: 'Do you want to deploy?', submitter: 'admin'
            }
          }
        }

        stage("DEV Deployment") {
            steps {
                deploy adapters: [tomcat9(credentialsId: 'bob', path: '', url: 'http://localhost:9191')], contextPath: null, war: '**/*.war'
            }
        }
    }
    post {
       failure {
         slackSend channel: 'exciting-shop-omatic-experience', message: 'Shop-omatic pipeline build failed', teamDomain: 'shop-omatic', tokenCredentialId: 'slacksecret'
       }
    }
}