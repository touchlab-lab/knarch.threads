//
// Created by Kevin Galligan on 7/11/18.
//

#include "Types.h"
#include "Natives.h"

extern "C" {

OBJ_GETTER(makeThreadOnlyCounter, void*);

// See Weak.kt for implementation details.
// Retrieve link on the counter object.
OBJ_GETTER(Konan_getThreadOnlyImpl, ObjHeader* referred) {
MetaObjHeader* meta = referred->meta_object();

if (meta->counter_ == nullptr) {
ObjHolder counterHolder;
// Cast unneeded, just to emphasize we store an object reference as void*.
ObjHeader* counter = makeThreadOnlyCounter(nullptr, counterHolder.slot());
UpdateRefIfNull(&meta->counter_, counter);
}
RETURN_OBJ(meta->counter_);
}

}