# Infrastructure (Terraform)

This folder keeps GCP infrastructure alongside the application so you can create, update, or destroy infra on demand.

## What it provisions

- Artifact Registry Docker repository
- GKE cluster and node pool
- GitHub Actions deployer service account
- GKE app service account for workload identity (`app-sa` in namespace)
- Workload Identity Pool + Provider for GitHub OIDC
- IAM bindings required for GitHub deploy workflow and GKE workload identity

## Folder

- `infra/terraform` - Terraform configuration

## Required GitHub Actions settings

Add these in repository settings (`Settings -> Secrets and variables -> Actions`):

- Secret or Variable: `GCP_PROJECT_ID`
- Secret or Variable: `TF_STATE_BUCKET` (existing GCS bucket for Terraform state)
- Optional Secret: `GCP_BOOTSTRAP_CREDENTIALS` (JSON key, only needed if you are not using existing WIF auth for infra workflow)
- Optional Variable: `TF_STATE_PREFIX` (defaults to `customer-service`)

For WIF auth in workflows (recommended after first apply), set:

- Secret or Variable: `WIF_PROVIDER`
- Secret or Variable: `WIF_SERVICE_ACCOUNT`

## On-demand infra lifecycle

Use workflow `.github/workflows/infra-terraform.yml` with `workflow_dispatch` input:

- `plan` - validate and plan only
- `apply` - create/update infrastructure
- `destroy` - destroy managed infrastructure

After `apply`, copy Terraform outputs into GitHub secrets/variables used by app deploy workflow.

