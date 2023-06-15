pipeline {

    agent any
    tools {
        maven 'M3'
    }
    stages {
        stage ('Checkout') {
            steps {
                checkout scmGit(branches: [[name: '*/develop']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Paris1985/shop-omatic']])
            }
        }

        stage ('Build') {
            steps {
                sh 'mvn clean install'
            }
            post {
                failure {
                  publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'target/site/jacoco', reportFiles: 'index.html', reportName: 'HTML Report', reportTitles: 'Shop-matic Code Coverage', useWrapperFileDirectly: true])
                  slackSend channel: 'develop', message: 'Build Failure', teamDomain: 'shop-omatic', tokenCredentialId: 'slack_token'
                }
            }
        }

        stage ('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                  sh 'mvn sonar:sonar -Dsonar.projectKey=shop-omatic-qa -Dsonar.projectName=shop-omatic-qa -Dsonar.host.url=http://localhost:9000 -Dsonar.token=squ_5ca2d5c8b04e7e20cd87d2b247035694d8b2d08b'
                }
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

        stage ('QA Approve') {
          steps {

          echo "Deployed to QA Approval"
          slackSend channel: 'develop', message: 'Dev deployment is needing your approval - http://localhost:9999/job/shop-omatic/', teamDomain: 'shop-omatic', tokenCredentialId: 'slack_token'

          echo "Taking approval from QA Manager for QA Deployment"
            timeout(time: 7, unit: 'DAYS') {
            input message: 'Do you want to deploy?', submitter: 'admin'
            }
          }
        }

        stage("QA Deployment") {
            steps {
               deploy adapters: [tomcat9(credentialsId: 'admin', path: '', url: 'http://localhost:9191')], contextPath: 'shop-omatic', war: '**/*.war'
            }
            post {
              failure {
                 slackSend channel: 'develop', message: 'Attention, QA Deployment was unsuccessful!', teamDomain: 'shop-omatic', tokenCredentialId: 'slack_token'
              }
            }
        }
    }
    post {
      success {
         slackSend channel: 'general', message: 'Congratulations, new build was successfully deployed!', teamDomain: 'shop-omatic', tokenCredentialId: 'slack_token'
      }
      failure {
         slackSend channel: 'general', message: 'Attention, Shop-omatic deployment was unsuccessful!', teamDomain: 'shop-omatic', tokenCredentialId: 'slack_token'
      }
    }
}
