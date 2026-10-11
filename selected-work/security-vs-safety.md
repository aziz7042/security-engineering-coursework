# Security and Safety in a Connected Laboratory

## Scenario

A university laboratory uses network-connected controllers to regulate temperature, ventilation and access to test equipment. Operators use authenticated workstations, while a monitoring service records measurements and alarms.

This short case study separates two related engineering goals:

- **Security** protects the system against intentional misuse, unauthorized access and manipulation.
- **Safety** protects people, equipment and the environment from unacceptable harm, including harm caused by faults or mistakes.

The goals overlap, but they are not interchangeable. A compromised controller is a security incident; unsafe temperature control is a safety hazard. One event can be both when malicious manipulation creates physical danger.

## Assets and unwanted outcomes

| Asset | Security concern | Safety concern |
|---|---|---|
| Controller configuration | Unauthorized changes | Dangerous operating limits |
| Sensor readings | Forgery, deletion or replay | Missed overheating or ventilation failure |
| Operator accounts | Credential theft or privilege escalation | Unsafe commands from an impersonated operator |
| Audit logs | Tampering and repudiation | Inability to reconstruct a dangerous event |
| Network availability | Denial of service | Loss of monitoring or delayed emergency response |

## Threat and hazard examples

1. An attacker reuses a stolen operator credential to change a temperature limit.
2. A failed sensor reports a plausible but incorrect value.
3. A network outage prevents remote monitoring.
4. A software update introduces an unsafe default configuration.
5. A legitimate operator selects the wrong device under time pressure.

Examples 1 and 5 can lead to the same hazardous state, even though only the first begins as an attack. The system therefore needs controls that cover both malicious and accidental causes.

## Layered controls

| Layer | Control | Primary purpose |
|---|---|---|
| Identity | Multi-factor authentication and least privilege | Limit unauthorized control |
| Communication | Authenticated encryption and replay protection | Preserve command confidentiality and integrity |
| Application | Range checks, two-person approval for critical limits | Reject unsafe or unauthorized changes |
| Device | Independent hard limits and fail-safe modes | Keep equipment safe if software or networking fails |
| Monitoring | Append-only audit events and anomaly alerts | Detect and investigate misuse or faults |
| Operations | Tested backup, recovery and emergency procedures | Restore service without bypassing safety controls |

## Verification approach

- Test authorization boundaries with normal and elevated accounts.
- Attempt tampering, replay and malformed-command scenarios.
- Inject sensor faults and communication outages.
- Verify that independent limits still place the equipment in a safe state.
- Confirm that logs contain enough information for incident reconstruction without storing secrets.
- Re-test safety assumptions after security updates and security assumptions after control-system changes.

## Conclusion

Security reduces the chance that an adversary can cause or hide a dangerous state. Safety engineering limits the harm even when prevention fails or no adversary exists. Treating both as system properties—and verifying their interaction—produces a stronger design than addressing either one in isolation.
