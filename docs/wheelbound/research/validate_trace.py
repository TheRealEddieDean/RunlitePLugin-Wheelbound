"""Offline capture-envelope validation only: never a live detector acceptance oracle."""
import argparse,datetime,json
from pathlib import Path
from jsonschema import Draft202012Validator
ROOT=Path(__file__).resolve().parent.parent
SCHEMA=json.loads((ROOT/'schemas/trace_capture.schema.json').read_text())
Draft202012Validator.check_schema(SCHEMA)
VALIDATOR=Draft202012Validator(SCHEMA)
def validate(data,allow_synthetic=False):
 VALIDATOR.validate(data)
 if not allow_synthetic and data['origin']!='LIVE_CAPTURE':raise ValueError('Synthetic examples cannot be accepted as live captures')
 timestamp=datetime.datetime.fromisoformat(data['captured_at_utc'].replace('Z','+00:00'))
 if timestamp.utcoffset()!=datetime.timedelta(0):raise ValueError('Capture timestamp must explicitly use UTC')
 events=data['events'];seq=[e['sequence'] for e in events]
 if seq!=list(range(len(events))):raise ValueError('Capture must have contiguous unique sequence numbers from zero')
 times=[e['monotonic_ms'] for e in events]
 if times!=sorted(times):raise ValueError('Capture monotonic callback ordering is inconsistent')
 epochs=[e['session_epoch'] for e in events]
 if epochs!=sorted(epochs):raise ValueError('Session epochs cannot move backward')
 if data['origin']=='LIVE_CAPTURE' and data['client_version'] in {'UNKNOWN','UNVERIFIED','latest.release'}:raise ValueError('Record actual resolved client version, not a dynamic dependency label')
 return {'status':'ENVELOPE_VALID_REVIEW_REQUIRED','origin':data['origin'],'events':len(events),'case_id':data['case_id'],'detector_accepted':False,'runtime_enabled':False}
def main():
 p=argparse.ArgumentParser();p.add_argument('path');p.add_argument('--allow-synthetic',action='store_true');a=p.parse_args()
 print(json.dumps(validate(json.loads(Path(a.path).read_text()),a.allow_synthetic)))
if __name__=='__main__':main()
