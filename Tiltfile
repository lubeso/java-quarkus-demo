modules = [
	{
		"name": "foo",
		"deploy": True,
	},
	{"name": "bar"},
]

for i, module in enumerate(modules):
	name = module.get("name")

	local_resource(
		labels=["development"],
		name="development-{}".format(name),
		serve_dir=module.get("serve_dir", name),
		serve_cmd=[
			"quarkus",
			"dev",
			"--debug-port={}".format(5000 + i),
		],
		serve_env=module.get("serve_env", {}) | {
			"QUARKUS_HTTP_TEST_PORT": "{}".format(6000 + i),
			"QUARKUS_HTTP_PORT": "{}".format(7000 + i),
		},
	)

	local_resource(
		labels=["builds"],
		name="build-{}".format(name),
		dir=module.get("dir", name),
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

	if module.get("deploy"):
		k8s_yaml(
			yaml="{}/build/kubernetes/kubernetes.yml".format(name),
		)
		k8s_resource(
			labels=["deployments"],
			workload=name,
			new_name="deployment-{}".format(name),
			port_forwards=[port_forward(8080 + i, 8080 + i, "http://")],
			auto_init=True,
			trigger_mode=TRIGGER_MODE_MANUAL
		)
