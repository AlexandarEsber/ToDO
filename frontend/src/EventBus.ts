import mitt from 'mitt';

type Events = {
  assigneeCreated: {};
  assigneeUpdated: {};
  toDoCreated: {};
  toDoUpdated: {}
};

const EventBus = mitt<Events>();

export default EventBus;