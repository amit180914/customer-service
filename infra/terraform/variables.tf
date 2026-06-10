variable "project_id" {
  description = "GCP project id"
  type        = string
}

variable "region" {
  description = "Primary GCP region for GKE and Artifact Registry"
  type        = string
  default     = "us-central1"
}

variable "gke_cluster_name" {
  description = "GKE cluster name"
  type        = string
  default     = "java-app-gke"
}

variable "gke_node_count" {
  description = "Node count in the default node pool"
  type        = number
  default     = 1
}

variable "gke_machine_type" {
  description = "Node machine type"
  type        = string
  default     = "e2-standard-2"
}

variable "gar_repository" {
  description = "Artifact Registry repository name"
  type        = string
  default     = "java-services"
}

variable "k8s_namespace" {
  description = "Kubernetes namespace used by the app"
  type        = string
  default     = "amit"
}

variable "github_repository" {
  description = "GitHub repository in owner/repo format"
  type        = string
}

variable "workload_identity_pool_id" {
  description = "Workload identity pool id"
  type        = string
  default     = "github-pool"
}

variable "workload_identity_provider_id" {
  description = "Workload identity provider id"
  type        = string
  default     = "github-provider"
}

variable "github_service_account_id" {
  description = "Service account id impersonated from GitHub Actions"
  type        = string
  default     = "github-terraform"
}

variable "gke_app_service_account_id" {
  description = "Service account id used by workload identity in GKE"
  type        = string
  default     = "gke-app-sa"
}

