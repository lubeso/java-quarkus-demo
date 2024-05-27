modules = [
	"foo",
	"bar",
]

for i, module in enumerate(modules):
	local_resource(
		labels=["development"],
		name="development-{}".format(module),
		serve_dir=module,
		serve_cmd=[
			"quarkus",
			"dev",
			"--debug-port={}".format(5000 + i),
		],
		serve_env={
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
		name="build-{}".format(module),
		dir=module,
		cmd=[
			"quarkus",
			"build",
		],
		env={
			"QUARKUS_HTTP_TEST_PORT": "{}".format(3000 + i),
			"QUARKUS_HTTP_PORT": "{}".format(4000 + i),
		},
		auto_init=True,
		trigger_mode=TRIGGER_MODE_MANUAL,
	)

	k8s_yaml(
		yaml="{}/build/kubernetes/kubernetes.yml".format(module),
	)
	k8s_resource(
		labels=["deployments"],
		workload=module,
		new_name="deployment-{}".format(module),
		port_forwards=[port_forward(8080 + i, 8080, "http://")],
		auto_init=True,
		trigger_mode=TRIGGER_MODE_MANUAL
	)
