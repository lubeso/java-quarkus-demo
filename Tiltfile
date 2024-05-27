load("ext://helm_resource", "helm_repo", "helm_resource")
helm_repo(
	"bitnami",
	"https://charts.bitnami.com/bitnami",
	labels=["database"],
)
helm_resource(
	"postgresql",
	"bitnami/postgresql",
	labels=["database"],
	resource_deps=["bitnami"],
	port_forwards=[port_forward(5432, 5432)],
)


modules = [
	{
		"name": "foo",
	},
	{
		"name": "bar",
		"env": {"QUARKUS_DATASOURCE_REACTIVE_URL": "postgresql://localhost:5432/postgres"},
		"serve_env": {"QUARKUS_DATASOURCE_REACTIVE_URL": "postgresql://localhost:5432/postgres"},
	},
]

for i, module in enumerate(modules):
	local_resource(
		labels=["development"],
		name="development-{}".format(module.get("name")),
		serve_dir=module.get("serve_dir", module.get("name")),
		serve_cmd=[
			"quarkus",
			"dev",
			"--debug-port={}".format(5000 + i),
		],
		serve_env=module.get("serve_env", {}) | {
			"QUARKUS_HTTP_TEST_PORT": "{}".format(6000 + i),
			"QUARKUS_HTTP_PORT": "{}".format(7000 + i),
		},
		links=[
			link(
				"http://localhost:{}".format(7000 + i),
				"http://",
			)
		],
	)

	local_resource(
		labels=["builds"],
		name="build-{}".format(module.get("name")),
		dir=module.get("dir", module.get("name")),
		cmd=[
			"quarkus",
			"build",
		],
		env=module.get("env", {}) | {
			"QUARKUS_HTTP_TEST_PORT": "{}".format(3000 + i),
			"QUARKUS_HTTP_PORT": "{}".format(4000 + i),
		},
		auto_init=True,
		trigger_mode=TRIGGER_MODE_MANUAL,
	)

	k8s_yaml(
		yaml="{}/build/kubernetes/kubernetes.yml".format(module.get("name")),
	)
	k8s_resource(
		labels=["deployments"],
		workload=module.get("name"),
		new_name="deployment-{}".format(module.get("name")),
		port_forwards=[port_forward(8080 + i, 8080, "http://")],
		auto_init=True,
		trigger_mode=TRIGGER_MODE_MANUAL
	)
