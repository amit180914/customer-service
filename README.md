# Customer Service

Spring Boot customer service with basic customer management APIs.

## Package Structure

`src/main/java/com/github/amit180914/customerservice`

- `controller`
- `service`
- `repository`
- `entity`
- `dto`
- `exception`
- `config`

## Endpoints

- `POST /api/customers` - Register customer
- `GET /api/customers/{customerId}` - Get customer details
- `PUT /api/customers/{customerId}` - Update customer profile
- `GET /api/customers` - List customers

### Sample Register Payload

```json
{
  "firstName": "Amit",
  "lastName": "Sharma",
  "mobileNumber": "9999999999",
  "email": "amit@example.com",
  "status": "ACTIVE"
}
```

### Sample Update Payload

```json
{
  "firstName": "Amitabh",
  "status": "INACTIVE"
}
```

## Run Tests

```powershell
.\mvnw.cmd test
```

## Run Application

```powershell
.\mvnw.cmd spring-boot:run
```

## Docker

### Build Image

```powershell
docker build -t customer-service:latest .
```

### Run PostgreSQL

```powershell
docker run --name postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=customerdb -e TZ=Asia/Kolkata -e PGTZ=Asia/Kolkata -p 5432:5432 -d postgres
```

### Run Customer Service Container

```powershell
docker run --name customer-service --rm -p 8080:8080 -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/customerdb?options=-c%20TimeZone%3DAsia%2FKolkata -e SPRING_DATASOURCE_USERNAME=postgres -e SPRING_DATASOURCE_PASSWORD=postgres customer-service:latest
```

## Maven Plugin For Docker Image (GCP)

This project includes `jib-maven-plugin` in `pom.xml` to build and push images without Dockerfile dependency.

### Build to Local Docker Daemon (from Maven)

```powershell
& "E:\tools\apache-maven-3.9.16\bin\mvn.cmd" compile jib:dockerBuild "-Dimage.name=customer-service" "-Dimage.tag=latest"
```

### Push to GCP Container Registry (`gcr.io`)

Short command using profile:

```powershell
gcloud auth configure-docker
& "E:\tools\apache-maven-3.9.16\bin\mvn.cmd" -Ppush-gcp package "-Dgcp.project.id=<your-project-id>" "-Dimage.name=customer-service" "-Dimage.tag=v1"
```

Equivalent explicit Jib command:

```powershell
gcloud auth configure-docker
& "E:\tools\apache-maven-3.9.16\bin\mvn.cmd" compile jib:build "-Dimage.registry=gcr.io" "-Dgcp.project.id=<your-project-id>" "-Dimage.name=customer-service" "-Dimage.tag=v1"
```

### Push to GCP Artifact Registry

Use registry format `<region>-docker.pkg.dev` and include repository in image name.

Short command using profile (recommended — uses OAuth2 token directly):

```powershell
$token = (& "C:\Users\hp\AppData\Local\Google\Cloud SDK\google-cloud-sdk\bin\gcloud.cmd" auth print-access-token)
Set-Location "E:\github\sb\customer-service"
& "E:\tools\apache-maven-3.9.16\bin\mvn.cmd" -Ppush-gcp-artifact package `
  "-Dgcp.project.id=<your-project-id>" `
  "-Dimage.name=<repo-name>/customer-service" `
  "-Dimage.tag=v1" `
  "-Djib.to.auth.username=oauth2accesstoken" `
  "-Djib.to.auth.password=$token"
```

Equivalent explicit Jib command:

```powershell
gcloud auth configure-docker asia-south1-docker.pkg.dev
& "E:\tools\apache-maven-3.9.16\bin\mvn.cmd" compile jib:build "-Dimage.registry=asia-south1-docker.pkg.dev" "-Dgcp.project.id=<your-project-id>" "-Dimage.name=<repo-name>/customer-service" "-Dimage.tag=v1"
```


## GitHub Actions Deploy to GKE

Workflow files:

- `.github/workflows/infra-terraform.yml` (plan/apply/destroy infra)
- `.github/workflows/deploy-gke.yml` (deploy app to existing infra)

### Infra Workflow (Create/Destroy on Demand)

Use `Infra Terraform` workflow with manual input:

- `plan` - check changes
- `apply` - create/update infra
- `destroy` - delete infra

Required for infra workflow:

- `GCP_PROJECT_ID` (secret or variable)
- `TF_STATE_BUCKET` (secret or variable; existing GCS bucket for Terraform state)
- Auth via either:
  - `GCP_BOOTSTRAP_CREDENTIALS` (secret, JSON key), or
  - `WIF_PROVIDER` + `WIF_SERVICE_ACCOUNT` (secret or variable)

After successful `apply`, copy Terraform outputs to app deploy secrets/variables.

### Required GitHub Secrets

Add these in your repository at `Settings -> Secrets and variables -> Actions`:

- `WIF_PROVIDER` - Workload Identity Provider resource name
- `WIF_SERVICE_ACCOUNT` - GitHub OIDC impersonation service account email
- `GCP_PROJECT_ID` - GCP project id
- `GKE_CLUSTER_NAME` - target GKE cluster name
- `GCP_APP_SA_EMAIL` - GCP service account email bound to K8s `app-sa`

You can store non-sensitive values as GitHub repository/environment variables instead of secrets.

### What the Workflow Does

- Builds image from `Dockerfile`
- Pushes image to Artifact Registry: `${GAR_LOCATION}-docker.pkg.dev/${GCP_PROJECT_ID}/${GAR_REPOSITORY}/java-app:${GITHUB_SHA}`
- Gets GKE credentials with `gcloud container clusters get-credentials`
- Renders placeholders in:
  - `k8s/deployment.yml` (`__IMAGE__`)
  - `k8s/serviceaccount.yml` (`__GCP_APP_SA_EMAIL__`)
- Applies manifests and waits for rollout

### Trigger Deployment

Manual run from Actions tab (`workflow_dispatch`) or push to `main`.

### Optional Pre-Check Commands

```powershell
kubectl get ns
kubectl get serviceaccount -n amit
kubectl get deploy,svc -n amit
```
