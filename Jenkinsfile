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
                failure {
                  publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'target/site/jacoco', reportFiles: 'index.html', reportName: 'HTML Report', reportTitles: 'Shop-matic Code Coverage', useWrapperFileDirectly: true])
                  //slackSend channel: 'develop', message: 'Build Failure', teamDomain: 'shop-omatic', tokenCredentialId: 'slack_token'
                }
            }
        }

        stage ('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                  sh 'mvn sonar:sonar -Dsonar.projectKey=shop-omatic -Dsonar.projectName=shop-omatic -Dsonar.host.url=http://localhost:9000 -Dsonar.token=sqp_63badec207ec5ebe2167523ce3a66d1a870ba474'
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

        stage ('PROD Approve') {
          steps {

          echo "Deployed to DEV Approval"
          //slackSend channel: 'develop', message: 'Dev deployment is needing your approval - http://localhost:9999/job/shop-omatic/', teamDomain: 'shop-omatic', tokenCredentialId: 'slack_token'

          echo "Taking approval from DEV Manager for QA Deployment"
            timeout(time: 7, unit: 'DAYS') {
            input message: 'Do you want to deploy?', submitter: 'admin'
            }
          }
        }

        stage("PROD Deployment") {
            steps {
               deploy adapters: [tomcat9(credentialsId: 'admin', path: '', url: 'http://localhost:9191')], contextPath: 'shop-omatic', war: '**/*.war'
            }
            post {
              failure {
                 //slackSend channel: 'develop', message: 'Attention, DEV Deployment was unsuccessful!', teamDomain: 'shop-omatic', tokenCredentialId: 'slack_token'
                 echo 'Attention, PROD Deployment was unsuccessful!'
              }
            }
        }
    }
    post {
      success {
           //slackSend channel: 'general', message: 'Congratulations, new build was successfully deployed!', teamDomain: 'shop-omatic', tokenCredentialId: 'slack_token'
          echo 'Congratulations, new build was successfully deployed!'
      }
      failure {
          //slackSend channel: 'general', message: 'Attention, Shop-omatic deployment was unsuccessful!', teamDomain: 'shop-omatic', tokenCredentialId: 'slack_token'
          echo 'Attention, PROD Deployment was unsuccessful!'
      }
    }
}
