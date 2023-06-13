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
                  slackSend channel: 'develop', message: 'Build Failure', teamDomain: 'shop-omatic', tokenCredentialId: 'slacksecret'
                }
            }
        }

        stage ('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar -Dsonar.projectKey=shop-omatic -Dsonar.projectName=shop-omatic -Dsonar.host.url=http://localhost:9000 -Dsonar.token=squ_25e462c45b1658f357c4944fa24599667d0bfba5'
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
          slackSend channel: 'develop', message: 'Dev deployment is needing your approval - http://localhost:9999/job/shop-omatic/', teamDomain: 'shop-omatic', tokenCredentialId: 'slacksecret'

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
            post {
               failure {
                 slackSend channel: 'develop', message: 'Attention, DEV Deployment was unsuccessful!', teamDomain: 'shop-omatic', tokenCredentialId: 'slacksecret'
               }
            }
        }
    }
    post {
       success {
         slackSend channel: 'general', message: 'Congratulations, new build was successfully deployed!', teamDomain: 'shop-omatic', tokenCredentialId: 'slacksecret'
       }
       failure {
         slackSend channel: 'general', message: 'Attention, Shop-omatic deployment was unsuccessful!', teamDomain: 'shop-omatic', tokenCredentialId: 'slacksecret'
       }
    }
}
