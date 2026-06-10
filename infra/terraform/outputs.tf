output "wif_provider" {
  description = "Workload Identity Provider full resource name"
  value       = google_iam_workload_identity_pool_provider.github.name
}

output "wif_service_account" {
  description = "Service account used by GitHub Actions"
  value       = google_service_account.github_deployer.email
}

output "gcp_project_id" {
  description = "GCP project id"
  value       = var.project_id
}

output "gke_cluster_name" {
  description = "GKE cluster name"
  value       = google_container_cluster.primary.name
}

output "gcp_app_service_account_email" {
  description = "GCP app service account email for KSA annotation"
  value       = google_service_account.gke_app.email
}

output "gar_location" {
  description = "Artifact Registry location"
  value       = var.region
}

output "gar_repository" {
  description = "Artifact Registry repository name"
  value       = google_artifact_registry_repository.java_services.repository_id
}

output "k8s_namespace" {
  description = "Kubernetes namespace used by app manifests"
  value       = var.k8s_namespace
}

